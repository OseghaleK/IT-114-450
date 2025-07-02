package M2;

// UCID: oka   Date: 07-02-25
// Plan: loop through the array to accumulate a running total,
// then print that total formatted with exactly two decimal places.

public class Problem2 extends BaseClass {

    private static double[] array1 = { 0.1, 0.2, 0.3, 0.4, 0.5, 0.6 };
    private static double[] array2 = { 1.0000001, 1.0000002, 1.0000003, 1.0000004, 1.0000005 };
    private static double[] array3 = { 1.0/3.0, 2.0/3.0, 4.0/3.0, 8.0/3.0, 8.0/3.0 };
    private static double[] array4 = { 1e16, 1.0, -1e16, 2.0, -2.0, 1e-16 };
    private static double[] array5 = {
        Math.PI, Math.E, Math.sqrt(2), Math.sqrt(3),
        Math.sqrt(5), Math.log(2), Math.log10(3)
    };

    private static void sumValues(double[] arr, int arrayNumber) {
        printArrayInfo(arr, arrayNumber);
        System.out.print("Output Array: ");
        // ─── Start Solution Edits ───────────────────────────────────────────────
        double total = 0;
        for (double v : arr) {
            total += v;
        }
        String formattedTotal = String.format("%.2f", total);
        System.out.print(formattedTotal);
        // ─── End   Solution Edits ───────────────────────────────────────────────
        System.out.println();
        System.out.println("______________________________________");
    }

    public static void main(String[] args) {
        final String ucid = "oka";
        printHeader(ucid, 2);
        sumValues(array1, 1);
        sumValues(array2, 2);
        sumValues(array3, 3);
        sumValues(array4, 4);
        sumValues(array5, 5);
        printFooter(ucid, 2);
    }
}
