package org.example.ai;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mistralai.MistralAiChatModel;
import org.springframework.ai.mistralai.MistralAiChatOptions;
import org.springframework.ai.mistralai.api.MistralAiApi;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.vaadin.firitin.fields.localized.Translator;

import java.util.Locale;

/**
 * Wires a Spring AI based {@link Translator} that Viritin's
 * {@code LocalizedTextField}/{@code LocalizedTextArea} pick up automatically
 * (they resolve a {@code Translator} through Vaadin's {@code Instantiator},
 * which in a Spring app means any {@code Translator} bean in the context).
 * <p>
 * The bean is only created when a Mistral API key is configured, so the demo
 * works without one too — the localized fields simply hide their "translate"
 * button when no {@link Translator} is available.
 * <p>
 * To enable translation, provide the key e.g. as an environment variable:
 * <pre>MISTRAL_API_KEY=... mvn spring-boot:run</pre>
 * Get a free key at <a href="https://console.mistral.ai">console.mistral.ai</a>.
 */
@Configuration
@ConditionalOnExpression("'${spring.ai.mistralai.api-key:}' != ''")
public class MistralTranslatorConfig {

    @Bean
    Translator mistralTranslator(@org.springframework.beans.factory.annotation.Value("${spring.ai.mistralai.api-key}") String apiKey) {
        MistralAiApi api = MistralAiApi.builder()
                .apiKey(apiKey)
                .build();
        MistralAiChatModel chatModel = MistralAiChatModel.builder()
                .mistralAiApi(api)
                .options(MistralAiChatOptions.builder()
                        .model(MistralAiApi.ChatModel.MISTRAL_SMALL)
                        .build())
                .build();
        ChatClient chatClient = ChatClient.create(chatModel);
        return new MistralTranslator(chatClient);
    }

    /**
     * Translates a single piece of text from one locale to another using an LLM.
     * Kept as a named class (rather than a lambda) so the prompt construction is
     * easy to read and tweak.
     */
    static class MistralTranslator implements Translator {

        private final ChatClient chatClient;

        MistralTranslator(ChatClient chatClient) {
            this.chatClient = chatClient;
        }

        @Override
        public String translate(String text, Locale from, Locale to) {
            if (text == null || text.isBlank()) {
                return text;
            }
            String fromLang = from.getDisplayLanguage(Locale.ENGLISH);
            String toLang = to.getDisplayLanguage(Locale.ENGLISH);
            return chatClient.prompt()
                    .system("""
                            You are a professional translation engine.
                            Translate the user's message from %s to %s.
                            Preserve meaning, tone and any formatting.
                            Reply with the translation ONLY — no quotes, no notes, no explanations.
                            """.formatted(fromLang, toLang))
                    .user(text)
                    .call()
                    .content();
        }
    }
}
