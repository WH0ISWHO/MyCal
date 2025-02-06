package RW01;

import javax.swing.JOptionPane;

class Cal4Tax {
    private final double value;
    private final double num;
    private final double Expenditure;
    private final double vatRate;
    private final double[] Investors;

    public Cal4Tax(double value, double Expenditure, double num, double vatRate, double[] investors) {// 생성자
        this.value = value;
        this.Expenditure = Expenditure;
        this.num = num;
        this.vatRate = vatRate;
        this.Investors = investors;
    }

    public void calculator() {// 계산 시작
        double expense = getExpense();
        double vat = getVat();
        double price = getPrice();

        StringBuilder mb = new StringBuilder();// e.g.) %,.2f원 = 12,345.00원

        mb.append(String.format("개당 희망가 : %,.0f원\n", this.value));
        mb.append(String.format("개수 : %,.0f개\n", this.num));
        mb.append(String.format("총 희망가 : %,.0f원\n\n", this.value*this.num));
        mb.append(String.format("총 경비 : %,.0f원\n(개당 경비 : %,.0f원)\n\n", (this.Expenditure * this.num), this.Expenditure));
        mb.append(String.format("세금 : %,.0f원\n(개당 세금 : %,.0f원)\n\n", vat * this.num, vat));
        mb.append(String.format("총 지출 비용 : %,.0f원\n(총 지출 = 경비 + 세금)\n\n", expense * this.num));
        mb.append(String.format("총 수익료 : %,.0f원\n(개당 수익료 : %,.0f원)\n\n", price * this.num, price));

        for (int i = 0; i < Investors.length; i++) {// 투자자의 수 만큼 반복
            double dividendRate = (Investors[i] * 100);
            mb.append(String.format("투자자 %d(%.2f%%) : %,.0f원\n", i + 1, dividendRate, (price * this.num) * Investors[i]) );
        }
        JOptionPane.showMessageDialog(null, mb.toString(), "결과", JOptionPane.INFORMATION_MESSAGE);
    }


    private double getExpense() {
        return Expenditure + getVat();
    }
    private double getVat() {
        return getValue() * vatRate;
    }
    private double getPrice() {
        return getValue() - getExpense();
    }
    private double getValue() {
        return value;
    }
}

class Start {
    public void getReady() {// 처음 윈도우
        while (true) {
            String[] options = {"수익 계산기", "계산기", "종료"};
            int choice = JOptionPane.showOptionDialog(null, "실행하실 목록을 선택하세요.", "Option",
                    JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);

            if (choice == 0) {// 수익 계산기
                Go();
            } else if (choice == 1) {// 계산기
                new Calculator();
                break;
            } else {// 종료
                JOptionPane.showMessageDialog(null, "프로그램을 종료합니다.", "종료", JOptionPane.INFORMATION_MESSAGE);
                break;
            }
        }
    }

    private void Go() {// 정보 입력
        double value;
        double Expenditure;
        double num;
        double vatRate;
        double[] investors;

        value = getBack("개당 희망가를 입력하세요.");
        if (value == -1) // 취소를 누르면 처음 윈도우로
            return;

        Expenditure = getBack("개당 경비를 입력하세요.");
        if (Expenditure == -1)
            return;

        num = getBack("개수를 입력하세요.");
        if (num == -1)
            return;

        vatRate = getBack("세율을 입력하세요.\n예) 10% = 0.1");
        if (vatRate == -1)
            return;

        int investorCount = (int)(getBack("투자자의 수를 입력하세요."));
        investors = new double[investorCount];// 사용자에게 선택권 주기 위해 배열로 입력받음

        for (int i = 0; i < investorCount; i++) {
            investors[i] = getBack(String.format("투자자 %d의 지분을 입력하세요.\n예) 10%% = 10", i + 1))/100;
            if (investors[i] == -1)
                return;
        }

        // 사용자가 다 입력과 출력을 해야 계산 시작!
        if (value != 0 && Expenditure != 0 && num != 0 && vatRate != 0) {
            Cal4Tax Cal = new Cal4Tax(value, Expenditure, num, vatRate, investors);// 생성자의 인자 값으로 지역 변수
            Cal.calculator();// 인스턴스 호출
        }
    }

    private double getBack(String message) {
        while (true) {
            try {
                String inputValue = JOptionPane.showInputDialog(message + "\n\n(취소를 누르면 처음으로 돌아갑니다.)");
                if (inputValue == null) // 사용자가 취소를 누를 경우
                    return -1;
                if (inputValue.equals("뒤로가기")) // 사용자가 뒤로가기를 선택할 경우
                    continue;
                return Double.parseDouble(inputValue);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(null, "잘못된 입력입니다. 숫자를 입력하세요.", "오류", JOptionPane.ERROR_MESSAGE);
            }
        }
    }
}

public class Main {// 희망 금액 = 원가 - 세금
    public static void main(String[] args) {
        Start st1 = new Start();// 인스턴스화
        st1.getReady();// ㄱㄱ
    }
}