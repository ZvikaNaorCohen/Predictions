package definition.value.generator.random.impl.string;

import definition.value.generator.random.api.AbstractRandomValueGenerator;

import java.util.ArrayList;
import java.util.List;

public class RandomStringGenerator  extends AbstractRandomValueGenerator<String> {
    String allowedChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!?,-.() ";
    @Override
    public String generateValue() {
        int length = random.nextInt(50) + 1;
        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int index = random.nextInt(allowedChars.length());
            char randomChar = allowedChars.charAt(index);
            sb.append(randomChar);
        }

        return sb.toString();

    }
}
