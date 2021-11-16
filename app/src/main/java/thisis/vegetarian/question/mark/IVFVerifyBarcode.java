package thisis.vegetarian.question.mark;


public class IVFVerifyBarcode {
    public static boolean verifyBarcode(String barcode){
        String[] checkValue = barcode.split("");
        //取的檢核碼(最後一位元)
        int verifyBarcode = Integer.parseInt(checkValue[checkValue.length - 1]);
        //偶數總和
        int oddSum = 0;
        //奇數總和
        int evenSum = 0;
        for (int i = checkValue.length - 2 ; i >= 0  ; i--){
            if ((i + 1) % 2 == 0){//這邊+1判斷位數順序
                evenSum += Integer.parseInt(checkValue[i]);
            } else {
                oddSum += Integer.parseInt(checkValue[i]);
            }
        }

        //將偶數乘三再與奇數總和
        int oddAddEven = (evenSum * 3) + oddSum;

        //取得總和後的最後一位元數字進行驗證
        int lastDigit = getLastDigit(oddAddEven);
        if (lastDigit == 0){
            return verifyBarcode == lastDigit;
        } else {
            return verifyBarcode == (10 - lastDigit);
        }
    }

    private static int getLastDigit(int in){
        return Math.abs(in) % 10;
    }
}
