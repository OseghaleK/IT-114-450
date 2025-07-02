package M2;

public class Problem4 extends BaseClass {
    private static String[] array1 = { "hello world!", "java programming", "special@#$%^&characters", "numbers 123 456",
            "mIxEd CaSe InPut!" };
    private static String[] array2 = { "hello world", "java programming", "this is a title case test",
            "capitalize every word", "mixEd CASE input" };
    private static String[] array3 = { "  hello   world  ", "java    programming  ",
            "  extra    spaces  between   words   ",
            "      leading and trailing spaces      ", "multiple      spaces" };
    private static String[] array4 = { "hello world", "java programming", "short", "a", "even" };

    private static void transformText(String[] arr, int arrayNumber) {
        // Only make edits between the designated "Start" and "End" comments
        printArrayInfoBasic(arr, arrayNumber);

        // Challenge 1: Remove non-alphanumeric characters except spaces
        // Challenge 2: Convert text to Title Case
        // Challenge 3: Trim leading/trailing spaces and remove duplicate spaces
        // Result 1-3: Assign final phrase to `placeholderForModifiedPhrase`
        // Challenge 4 (extra credit): Extract middle 3 characters (beginning starts at middle of phrase),
        // assign to 'placeholderForMiddleCharacters'
        // if not enough characters assign "Not enough characters"

        // Step 1: sketch out plan using comments (include ucid and date)
        //oka -07-02-25
        // Plan:
        // (1) Clean and format each phrase by removing non-alphanumeric characters (except spaces),
        //     trimming spaces, converting to Title Case, and collapsing duplicate spaces.
        // (2) Store the result in placeholderForModifiedPhrase.
        // (3) If the cleaned phrase is at least 3 characters, extract the middle 3 characters.
        //     Start the extraction from one character before the middle index.
        // (4) If not enough characters, assign "Not enough characters".

        String placeholderForModifiedPhrase = "";
        String placeholderForMiddleCharacters = "";

        for(int i = 0; i < arr.length; i++) {
            // Start Solution Edits
            //oka -07-02-25
            String phrase = arr[i];

            // Step 1: Remove non-alphanumeric except spaces
            phrase = phrase.replaceAll("[^a-zA-Z0-9 ]", "");

            // Step 2: Trim and collapse duplicate spaces
            phrase = phrase.trim().replaceAll("\\s{2,}", " ");

            // Step 3: Convert to Title Case
            StringBuilder t = new StringBuilder();
            for (String w : phrase.split(" ")) {
                if (!w.isEmpty()) {
                    t.append(Character.toUpperCase(w.charAt(0)))
                     .append(w.substring(1).toLowerCase())
                     .append(" ");
                }
            }
            placeholderForModifiedPhrase = t.toString().trim();

            // Step 4: Extract middle 3 characters
            if (placeholderForModifiedPhrase.length() >= 3) {
                int mid = placeholderForModifiedPhrase.length() / 2;
                if (placeholderForModifiedPhrase.length() % 2 == 0) {
                    mid -= 1;
                }
                placeholderForMiddleCharacters = placeholderForModifiedPhrase.substring(mid - 1, mid + 2);
            } else {
                placeholderForMiddleCharacters = "Not enough characters";
            }
            // End Solution Edits

            System.out.println(String.format("Index[%d] \"%s\" | Middle: \"%s\"", i, placeholderForModifiedPhrase, placeholderForMiddleCharacters));
        }

        System.out.println("\n______________________________________");
    }

    public static void main(String[] args) {
        final String ucid = "oka"; // <-- change to your UCID
        // No edits below this line
        printHeader(ucid, 4);

        transformText(array1, 1);
        transformText(array2, 2);
        transformText(array3, 3);
        transformText(array4, 4);
        printFooter(ucid, 4);
    }
}
