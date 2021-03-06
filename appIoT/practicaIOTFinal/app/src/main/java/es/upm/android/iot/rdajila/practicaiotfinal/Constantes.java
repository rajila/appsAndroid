package es.upm.android.iot.rdajila.practicaiotfinal;

import com.google.android.things.pio.Gpio;

import java.util.HashMap;

public final class Constantes
{
    public static final int REST = -1;

    // Melodia Star Wars
    public static final int c = 261; // led Yellow
    public static final int d = 294; // led Blue
    public static final int e = 329; // led Red
    public static final int f = 349; // led Green
    public static final int g = 391; // led White
    public static final int gS = 415;
    public static final int a = 440; // led Yellow
    public static final int aS = 455;
    public static final int b = 466; // led Blue
    public static final int cH = 523;
    public static final int cSH = 554;
    public static final int dH = 587;
    public static final int dSH = 622;
    public static final int eH = 659;
    public static final int fH = 698;
    public static final int fSH = 740;
    public static final int gH = 784;
    public static final int gSH = 830;
    public static final int aH = 880;

    public static final String PINBUZZER = "PWM1"; // NUM PIN 33

    public static final String PINBUTTON = "BCM21"; // NUM PIN 40

    public static final String PINRED = "BCM5"; // NUM PIN 29
    public static final String PINGREEN = "BCM6"; // NUM PIN 31
    public static final String PINBLUE = "BCM17"; // NUM PIN 11
    public static final String PINYELLOW = "BCM27"; // NUM PIN 13
    public static final String PINWHITE = "BCM22"; // NUM PIN 15

    public static HashMap<Integer,Note> initToneStarWars(Gpio ledYellow, Gpio ledBlue, Gpio ledRed, Gpio ledGreen, Gpio ledWhite )
    {
        HashMap<Integer,Note> _mapTmp = new HashMap<Integer,Note>();

        _mapTmp.put(0, new Note(a,500,ledYellow));
        _mapTmp.put(1,new Note(a, 500,ledYellow));
        _mapTmp.put(2,new Note(a, 500,ledYellow));
        _mapTmp.put(3,new Note(f, 350, ledGreen));
        _mapTmp.put(4,new Note(cH, 150, ledYellow));
        _mapTmp.put(5,new Note(a, 500, ledYellow));
        _mapTmp.put(6,new Note(f, 350, ledGreen));
        _mapTmp.put(7,new Note(cH, 150, ledYellow));
        _mapTmp.put(8,new Note(a, 650, ledYellow));

        _mapTmp.put(9,new Note(REST, 500, ledWhite));

        _mapTmp.put(10,new Note(eH, 500, ledRed));
        _mapTmp.put(11,new Note(eH, 500, ledRed));
        _mapTmp.put(12,new Note(eH, 500, ledRed));
        _mapTmp.put(13,new Note(fH, 350, ledGreen));
        _mapTmp.put(14,new Note(cH, 150, ledYellow));
        _mapTmp.put(15,new Note(gS, 500, ledWhite));
        _mapTmp.put(16,new Note(f, 350,ledGreen));
        _mapTmp.put(17,new Note(cH, 150,ledYellow));
        _mapTmp.put(18,new Note(a, 650,ledYellow));

        _mapTmp.put(19,new Note(REST, 500, ledWhite));

        _mapTmp.put(20,new Note(aH, 500, ledYellow));
        _mapTmp.put(21,new Note(a, 300, ledYellow));
        _mapTmp.put(22,new Note(a, 150, ledYellow));
        _mapTmp.put(23,new Note(aH, 500, ledYellow));
        _mapTmp.put(24,new Note(gSH, 325, ledWhite));
        _mapTmp.put(25,new Note(gH, 175, ledWhite));
        _mapTmp.put(26,new Note(fSH, 125, ledGreen));
        _mapTmp.put(27,new Note(fH, 125, ledGreen));
        _mapTmp.put(28,new Note(fSH, 250, ledGreen));

        _mapTmp.put(29,new Note(REST, 325, ledWhite));

        _mapTmp.put(30,new Note(aS, 250, ledYellow));
        _mapTmp.put(31,new Note(dSH, 500, ledBlue));
        _mapTmp.put(32,new Note(dH, 325, ledBlue));
        _mapTmp.put(33,new Note(cSH, 175, ledYellow));
        _mapTmp.put(34,new Note(cH, 125, ledYellow));
        _mapTmp.put(35,new Note(b, 125, ledBlue));
        _mapTmp.put(36,new Note(cH, 250, ledYellow));

        _mapTmp.put(37, new Note(REST, 350, ledWhite));

        _mapTmp.put(38,new Note(f, 250, ledGreen));
        _mapTmp.put(39,new Note(gS, 500, ledWhite));
        _mapTmp.put(40,new Note(f, 350, ledGreen));
        _mapTmp.put(41,new Note(a, 125, ledYellow));
        _mapTmp.put(42,new Note(cH, 500, ledYellow));
        _mapTmp.put(43,new Note(a, 375, ledYellow));
        _mapTmp.put(44,new Note(cH, 125, ledYellow));
        _mapTmp.put(45,new Note(eH, 650, ledRed));

        _mapTmp.put(46,new Note(REST,500, ledWhite));

        _mapTmp.put(47,new Note(aH, 500, ledYellow));
        _mapTmp.put(48,new Note(a, 300, ledYellow));
        _mapTmp.put(49,new Note(a, 150, ledYellow));
        _mapTmp.put(50,new Note(aH, 500, ledYellow));
        _mapTmp.put(51,new Note(gSH, 325, ledWhite));
        _mapTmp.put(52,new Note(gH, 175, ledWhite));
        _mapTmp.put(53,new Note(fSH, 125, ledWhite));
        _mapTmp.put(54,new Note(fH, 125, ledGreen));
        _mapTmp.put(55,new Note(fSH, 250, ledGreen));

        _mapTmp.put(56,new Note(REST, 325, ledWhite));

        _mapTmp.put(57,new Note(aS, 250, ledYellow));
        _mapTmp.put(58,new Note(dSH, 500, ledBlue));
        _mapTmp.put(59,new Note(dH, 325, ledBlue));
        _mapTmp.put(60,new Note(cSH, 175, ledYellow));
        _mapTmp.put(61,new Note(cH, 125, ledYellow));
        _mapTmp.put(62,new Note(b, 125, ledBlue));
        _mapTmp.put(63,new Note(cH, 250, ledYellow));

        _mapTmp.put(64, new Note(REST, 350, ledWhite));

        _mapTmp.put(65,new Note(f, 250, ledGreen));
        _mapTmp.put(66,new Note(gS, 500, ledWhite));
        _mapTmp.put(67,new Note(f, 375, ledGreen));
        _mapTmp.put(68,new Note(cH, 125, ledYellow));
        _mapTmp.put(69,new Note(a, 500, ledYellow));
        _mapTmp.put(70,new Note(f, 375, ledGreen));
        _mapTmp.put(71,new Note(cH, 125, ledYellow));
        _mapTmp.put(72,new Note(a, 650, ledYellow));

        _mapTmp.put(73,new Note(REST,650, ledWhite));

        return _mapTmp;
    }
}