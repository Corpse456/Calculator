package calculator;

import java.util.*;

public class Polish {
    // доступные операторы
    public static final String OPERATORS = "+-*/^";
    // преобразованная строка
    private static List<String> polishEntry;
    // временный стек для операторов и скобок
    private static List<Character> stek;

    public static double refactoring(String input) {
	int nextNumberStart = 0;
	stek = new LinkedList<Character>();
	polishEntry = new LinkedList<String>();

	int start = 0;
	// если минус вначале выражения, то он - часть числа, а не
	// оператор в данном случае
	if (input.charAt(0) == '-') {
	    start++;
	}

	for (int i = start; i < input.length(); i++) {
	    // открывающая скобка просто помещается в стек
	    if (input.charAt(i) == '(') {
		stek.add(input.charAt(i));
		// начало следующего числа - после открывающей скобки
		nextNumberStart++;
		// если минус после открывающей скобки, то он часть числа, а не
		// оператор в данном случае
		if (input.charAt(i + 1) == '-') {
		    i++;
		}
	    } else if (input.charAt(i) == ')') {
		// добавить последнее число внутри скобок в polsihEntry
		polishEntryAdd(input.substring(nextNumberStart, i));
		// пока не встретится открывающая скобка - добавлять операторы
		// из стека в PolishEntry
		while ('(' != stek.get(stek.size() - 1)) {
		    polishEntry.add("" + stek.get(stek.size() - 1));
		    stek.remove(stek.size() - 1);
		}
		// удалить открывающую скобку из стека
		stek.remove(stek.size() - 1);
		// начало следующего числа - после закрывающей скобки
		nextNumberStart = i + 1;
	    } else if (OPERATORS.contains(input.substring(i, i + 1))) {
		polishEntryAdd(input.substring(nextNumberStart, i));
		// если стек не пуст и последний элемент в нём оператор c большим или равным приоритетом,
		if (!stek.isEmpty() && priority(input.charAt(i)) <= priority(stek.get(stek.size() - 1))) {
		    // переместить этот оператор в выходную строку,
		    polishEntry.add("" + stek.get(stek.size() - 1));
		    stek.remove(stek.size() - 1);
		}
		// а текущий оператор добавить в стек
		stek.add(input.charAt(i));
		// начало следующего числа после оператора
		nextNumberStart = i + 1;
	    }
	}

	// добавить в выходную строку последнее число
	polishEntryAdd(input.substring(nextNumberStart, input.length()));
	while (!stek.isEmpty()) {
	    // и переместить все операторы из стека
	    polishEntry.add("" + stek.get(stek.size() - 1));
	    stek.remove(stek.size() - 1);
	}

	return calculate();
    }

    private static double calculate() {
	//for (String next : polishEntry) System.out.print(next + "|");
	//System.out.println();
	for (int i = 0; i < polishEntry.size(); i++) {
	    if (OPERATORS.contains(polishEntry.get(i))) {
		switch (polishEntry.get(i)) {
		case "+": {
		    polishEntry.set((i - 2), "" + (Double.parseDouble(polishEntry.get(i - 2))
			    + Double.parseDouble(polishEntry.get(i - 1))));
		    break;
		}
		case "-": {
		    polishEntry.set((i - 2), "" + (Double.parseDouble(polishEntry.get(i - 2))
			    - Double.parseDouble(polishEntry.get(i - 1))));
		    break;
		}
		case "*": {
		    polishEntry.set((i - 2), "" + (Double.parseDouble(polishEntry.get(i - 2))
			    * Double.parseDouble(polishEntry.get(i - 1))));
		    break;
		}
		case "/": {
		    polishEntry.set((i - 2), "" + (Double.parseDouble(polishEntry.get(i - 2))
			    / Double.parseDouble(polishEntry.get(i - 1))));
		    break;
		}
		case "^": {
		    polishEntry.set((i - 2), "" + Math.pow(Double.parseDouble(polishEntry.get(i - 2)),
			    Double.parseDouble(polishEntry.get(i - 1))));
		    break;
		}
		}
		polishEntry.remove(i);
		polishEntry.remove(i - 1);
		i = 0;
	    }
	}
	return Double.parseDouble(polishEntry.get(0));
    }

    private static void polishEntryAdd(String substring) {
	if (substring.length() == 0) {
	    return;
	}

	polishEntry.add(substring);
    }

    private static int priority(char operator) {
	if (operator == '+' || operator == '-') {
	    return 0;
	} else if (operator == '*' || operator == '/') {
	    return 1;
	} else if (operator == '(') {
	    return -1;
	}
	return 2;
    }
}