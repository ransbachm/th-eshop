import os, sys
import pandas as pd
import numpy as np
from math import sqrt
from sqlalchemy import create_engine, text
import pymysql
import dotenv
from urllib.parse import quote_plus
dotenv.load_dotenv(dotenv_path="../.env")

# Takes a lot of code from https://medium.com/swlh/how-to-build-simple-recommender-systems-in-python-647e5bcd78bd
 
sql_host = "host.docker.internal" #os.environ['SQL_HOST']
sql_user = os.environ['SQL_DB_USER']
sql_pwd = quote_plus(os.environ['SQL_DB_PWD']) # Escape special chars
sql_db = os.environ['SQL_DB']


sql_engine = create_engine(f"mysql+pymysql://'{sql_user}':{sql_pwd}@{sql_host}/{sql_db}")
conn = sql_engine.connect()

user_id = int(sys.argv[1])


ratings_sql = f"""
-- selects rating or 3 for every item that was ordered

select User.id AS user, Rating.product AS product, Rating.rating
	from Rating 
	join User on Rating.user = User.id 
   join Product on Rating.product = Product.id

UNION -- if not rated assume 3 rating

SELECT `Order`.`user` AS user, Product.id AS product, 3 AS rating
	FROM OrderItem
	JOIN Product ON OrderItem.product = Product.id
	JOIN `Order` ON OrderItem.`order` = `Order`.id
	
	
	Where 
	
	NOT
	(
		(Product.id IN (
		 	SELECT DISTINCT Rating.product AS Product
		 		FROM Rating
				JOIN User on Rating.user = User.id 
   			JOIN Product on Rating.product = Product.id
		) 	
	AND
		`Order`.`user` IN (
		 	SELECT DISTINCT Rating.user AS Product
		 		FROM Rating
				JOIN User on Rating.user = User.id 
   			JOIN Product on Rating.product = Product.id
			)
		)
	)
"""


ratings_df = pd.read_sql(text(ratings_sql), conn)
input_products = ratings_df[ratings_df.user == user_id]


user_subset = ratings_df[ratings_df["product"].isin(input_products["product"].tolist())]
user_subset = user_subset[user_subset["user"] != user_id]

user_subset_group = user_subset.groupby(["user"])

user_subset_group = sorted(user_subset_group, key=lambda x: len(x[1]), reverse=True)

user_subset_group = user_subset_group[0:100]


# THe pearson correlation coefficient is not defined for constant relationships so
# if two users share two ratings which are exactly the same it is undefined.
# To counteract this we add a minimal bias to create a linear relationship
# which does not affect the result in a meaningful way
def add_tiny_delta(arr):
    epsilon = 1e-6
    return arr + epsilon * np.random.randn(len(arr))


pearsonCorrelationDict = {}


for name, group in user_subset_group:
    group = group.sort_values(by="product")

    input_products = input_products.sort_values(by="product")

    n_ratings = len(group)

    temp_df = input_products[input_products["product"].isin(group["product"].tolist())]

    temp_ratings_list = add_tiny_delta(temp_df["rating"].tolist())

    temp_group_list = add_tiny_delta(group["rating"].tolist())

    # coefficient is also not defined for only one shared rating
    if temp_ratings_list.size < 2:
        if temp_ratings_list.size == 1:
            # *very* basic aproximation
            diff = temp_group_list[0] - temp_ratings_list[0]
            rating = 1 / (np.abs(diff) + 1)
            pearsonCorrelationDict[name] = rating  
        continue

    Sxx = sum([i**2 for i in temp_ratings_list]) \
        - pow(sum(temp_ratings_list), 2)/float(n_ratings)

    Syy = sum([i**2 for i in temp_group_list]) \
        - pow(sum(temp_group_list), 2)/float(n_ratings)

    Sxy = sum(i*j for i,j in zip(temp_ratings_list, temp_group_list)) \
        - sum(temp_ratings_list) * sum(temp_group_list) /float(n_ratings)
    
    
    if Sxx != 0 and Syy != 0:
        pearsonCorrelationDict[name] = Sxy/sqrt(Sxx*Syy)
    else:
        pearsonCorrelationDict[name] = 0    

if(len(pearsonCorrelationDict) == 0):
    exit(0)

pearson_df = pd.DataFrame.from_dict(pearsonCorrelationDict, orient="index")
pearson_df.columns = ["similarity_index"]
pearson_df["user"] = pearson_df.index
pearson_df.index = range(len(pearson_df))

top_user = pearson_df.sort_values(by="similarity_index", ascending=False)[0:50]

top_user_rating = top_user.merge(ratings_df, left_on="user", 
    right_on="user", how="inner")

top_user_rating["weighed_rating"] = top_user_rating["similarity_index"] * top_user_rating["rating"]

temp_top_user_rating = top_user_rating.groupby("product").\
        sum()[["similarity_index","weighed_rating"]]

temp_top_user_rating.columns = ["sum_similarity_index", "sum_weighed_rating"]

recommendation_df = pd.DataFrame()

recommendation_df["weighed_average recommendation score"] =  \
    temp_top_user_rating["sum_weighed_rating"] /  \
    temp_top_user_rating["sum_similarity_index"]

recommendation_df["product"] = recommendation_df.index

recommendation_df = recommendation_df.sort_values(by= 
    "weighed_average recommendation score",
    ascending=False)
recommendation_df.pop(recommendation_df.columns[1])

res = recommendation_df.to_csv(lineterminator="\n",sep=",",header=False)
print(res)

