package M2;

public class Problem1 extends BaseClass {
    private static int[] array1 = {0,1,2,3,4,5,6,7,8,9};   
    private static int[] array2 = {9,8,7,6,5,4,3,2,1,0};
    private static int[] array3 = {0,0,1,1,2,2,3,3,4,4,5,5,6,6,7,7,8,8,9,9};
    private static int[] array4 = {9,9,8,8,7,7,6,6,5,5,4,4,3,3,2,2,1,1,0,0}; 

    private static void printOdds(int[] arr, int arrayNumber) {
        // Only make edits between the designated "Start" and "End" comments
        printArrayInfo(arr, arrayNumber);

        // Challenge: Print odd values only in a single line separated by commas
        System.out.print("Output Array: ");
        // ─── Start Solution Edits ───────────────────────────────────────────────
        // oka – 07-02-25  Plan: loop through arr, build comma-separated list of odds,
        // then drop the final “, ” before printing
        StringBuilder sb = new StringBuilder();
        for (int n : arr) {
            if (n % 2 != 0) {
                sb.append(n).append(", ");
            }
        }
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 2);  // remove trailing comma+space
        }
        System.out.print(sb);
        // ─── End   Solution Edits ───────────────────────────────────────────────

        System.out.println();
        System.out.println("______________________________________");
    }

    public static void main(String[] args) {
        final String ucid = "oka"; // <-- change to your UCID if needed
        // no edits below this line
        printHeader(ucid, 1);
        printOdds(array1, 1);
        printOdds(array2, 2);
        printOdds(array3, 3);
        printOdds(array4, 4);
        printFooter(ucid, 1);
    }
}
