package server;

public class StringProcessor {

    /**
     * Обрабатывает входную строку, заключая все найденные цифры в квадратные скобки.
     *
     * @param input Входная строка.
     * @return Обработанная строка с цифрами в квадратных скобках.
     */
    public static String process(String input) {
        if (input == null) {
            return null; // Или бросить исключение, по желанию
        }

        StringBuilder result = new StringBuilder();
        for (char c : input.toCharArray()) {
            if (Character.isDigit(c)) {
                result.append('[').append(c).append(']');
            } else {
                result.append(c);
            }
        }
        return result.toString();
    }

    // Для тестирования метода (по желанию)
    // public static void main(String[] args) {
    //     System.out.println(process("a1b2")); // Выведет: a[1]b[2]
    //     System.out.println(process("hello123world")); // Выведет: hello[1][2][3]world
    //     System.out.println(process("no digits here!")); // Выведет: no digits here!
    //     System.out.println(process("")); // Выведет: (пустая строка)
    //     System.out.println(process(null)); // Выведет: null
    // }
}