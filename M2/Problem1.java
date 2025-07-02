package M2;

// UCID: oka   Date: 07-02-25
// Plan: loop through the array, collect odd numbers in a StringBuilder,
// trim the final “, ”, then print everything on one line.

public class Problem1 extends BaseClass {

    private static final int[] array1 = {0,1,2,3,4,5,6,7,8,9};
    private static final int[] array2 = {9,8,7,6,5,4,3,2,1,0};
    private static final int[] array3 = {0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9};
    private static final int[] array4 = {9,9,8,8,7,7,6,6,5,5,4,4,3,3,2,2,1,1,0,0};

    /** Prints the odd values of {@code arr} in one comma-separated line. */
    private static void printOdds(int[] arr, int arrayNumber) {
        // Only make edits between the designated "Start" and "End" comments
        printArrayInfo(arr, arrayNumber);

        System.out.print("Output Array: ");
        // ─── Start Solution Edits ───────────────────────────────────────────────
        // oka – 07-02-25: Build a list of odd numbers, separated by ", "
        StringBuilder odds = new StringBuilder();
        for (int n : arr) {
            if (n % 2 != 0) odds.append(n).append(", ");
        }
        if (odds.length() > 0) odds.setLength(odds.length() - 2); // drop last ", "
        System.out.print(odds);
        // ─── End   Solution Edits ───────────────────────────────────────────────
        System.out.println();
        System.out.println("______________________________________");
    }

    public static void main(String[] args) {
        final String ucid = "oka"; // ←-- change to your UCID if different
        // no edits below this line
        printHeader(ucid, 1);
        printOdds(array1, 1);
        printOdds(array2, 2);
        printOdds(array3, 3);
        printOdds(array4, 4);
        printFooter(ucid, 1);
    }
}
