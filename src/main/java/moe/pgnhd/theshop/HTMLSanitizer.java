package moe.pgnhd.theshop;

import org.owasp.html.HtmlPolicyBuilder;
import org.owasp.html.PolicyFactory;
import org.owasp.html.Sanitizers;

public class HTMLSanitizer {
    PolicyFactory escape;
    PolicyFactory text_only;
    public HTMLSanitizer() {
        PolicyFactory lists = new HtmlPolicyBuilder()
                .allowElements("ol", "ul", "li")
                .toFactory();

        escape = Sanitizers.
                FORMATTING
                .and(lists);

        text_only = new HtmlPolicyBuilder()
                .disallowElements()
                .toFactory();
    }

    public String sanitize(String string) {
        return escape.sanitize(string);
    }

    public String text_only(String string) {
        return text_only.sanitize(string);
    }
}
