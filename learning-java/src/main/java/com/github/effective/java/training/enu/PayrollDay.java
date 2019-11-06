package com.github.effective.java.training.enu;

/**
 * @author BirdSnail
 * @date 2019/11/4
 */
public enum  PayrollDay {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY(PayType.WEEKEND),
    SUNDAY(PayType.WEEKEND);

    private final PayType payType;

    private PayrollDay() {
        payType = PayType.WEEKDAY;
    }

    private PayrollDay(PayType payType) {
        this.payType = payType;
    }

    public int pay(int minutesWorked, int payRate) {
        return payType.pay(minutesWorked, payRate);
    }

    private enum PayType{
        WEEKDAY{
            @Override
            int overtimePay(int minutesWorked, int payRate) {
                return minutesWorked <= MINS_REP_SHIFT ? 0 : (minutesWorked - MINS_REP_SHIFT) * payRate / 2;
            }
        },
        WEEKEND{
            @Override
            int overtimePay(int minutesWorked, int payRate) {
                return minutesWorked * payRate / 2;
            }
        };

        private static final int MINS_REP_SHIFT = 8 * 60;

        abstract int overtimePay(int minutesWorked, int payRate);

        int pay(int minutesWorked, int payRate) {
            int basePay = minutesWorked * payRate;
            return basePay + overtimePay(minutesWorked, payRate);
        }
    }
}
