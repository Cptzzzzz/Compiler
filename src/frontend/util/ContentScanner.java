package frontend.util;

import frontend.lexical.Lexicality;

public class ContentScanner {
    private ContentScanner() {
    }

    private static ContentScanner contentScanner;

    public static ContentScanner getInstance() {
        if (contentScanner == null) {
            contentScanner = new ContentScanner();
        }
        return contentScanner;
    }

    public void start(String content) {
        content = content + " ";
        int length = content.length();
        int line = 1;
        String shuts = " \t\r\n+-*/%;,()[]{}";
        String word = "";
        int state = 0;
        for (int i = 0; i < length; i++) {
            String s = content.substring(i, i + 1);
            if (state == 1) {// 双引号字符串
                word += s;
                if (s.equals("\"")) {
                    state = 0;
                    Lexicality.solve(word, line);
                    word = "";
                }
                continue;
            }
            if (s.equals("\"")) {
                Lexicality.solve(word, line);
                word = s;
                state = 1;
                continue;
            }
            if (shuts.contains(s)) {
                Lexicality.solve(word, line);
                if (s.equals("\n")) line++;
                word = "";
                Lexicality.solve(s, line);
                continue;
            }

            if (!word.equals("")) {
                if (word.matches("^[\\_a-zA-Z][\\_a-z0-9A-Z]*")) {//是一个字符串
                    if (s.matches("[\\_a-z0-9A-Z]")) {
                        word += s;
                        continue;
                    } else {
                        Lexicality.solve(word, line);
                    }
                } else if (word.matches("^(0|[1-9][0-9]*)$")) {//是一个数字
                    if (s.matches("[0-9]*")) {
                        word += s;
                        continue;
                    } else {
                        Lexicality.solve(word, line);
                    }
                } else if (word.equals("<") || word.equals(">") || word.equals("=")) {
                    if (s.equals("=")) {
                        word += s;
                        Lexicality.solve(word, line);
                        word = "";
                        continue;
                    } else {
                        Lexicality.solve(word, line);
                    }
                } else if (word.equals("!")) {
                    if (s.equals("=")) {
                        word += s;
                        Lexicality.solve(word, line);
                        word = "";
                        continue;
                    } else {
                        Lexicality.solve(word, line);
                    }
                } else if (word.equals("&")) {
                    if (s.equals("&")) {
                        word += s;
                        Lexicality.solve(word, line);
                        word = "";
                        continue;
                    } else {
                        s = word + s;
                    }
                } else if (word.equals("|")) {
                    if (s.equals("|")) {
                        word += s;
                        Lexicality.solve(word, line);
                        word = "";
                        continue;
                    } else {
                        s = word + s;
                    }
                } else {
                    s = word + s;
                }
            }

            word = s;
        }
    }

    public String pretreat(String content) {
        StringBuilder res = new StringBuilder();
        int length = content.length();
        int state = 0;
        for (int i = 0; i < length; i++) {
            String s = content.substring(i, i + 1);
            if (state == 0) {
                if (s.equals("\"")) {
                    state = 1;
                    res.append(s);
                } else if (s.equals("/")) {
                    state = 2;
                } else {
                    res.append(s);
                }
            } else if (state == 1) {
                if (s.equals("\"")) {
                    state = 0;
                }
                res.append(s);
            } else if (state == 2) {
                if (s.equals("/")) {
                    res.append("  ");
                    state = 3;
                } else if (s.equals("*")) {
                    res.append("  ");
                    state = 4;
                } else {
                    res.append("/");
                    res.append(s);
                    state = 0;
                }
            } else if (state == 3) {
                if (s.equals("\n")) {
                    state = 0;
                    res.append("\n");
                } else {
                    res.append(" ");
                }
            } else if (state == 4) {
                if (s.equals("*")) {
                    state = 5;
                } else if (s.equals("\n")) {
                    res.append("\n");
                } else {
                    res.append(" ");
                }
            } else {
                switch (s) {
                    case "*":
                        res.append(" ");
                        break;
                    case "/":
                        res.append("  ");
                        state = 0;
                        break;
                    case "\n":
                        res.append("\n");
                        state = 4;
                        break;
                    default:
                        res.append(" ");
                        state = 4;
                        break;
                }
            }
        }
        return res.toString();
    }

    public static void main(String[] args) {

    }
}
