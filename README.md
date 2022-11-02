# Compiler-Mid-Exam

​	2022编译技术实验 - 期中考核   分值 15,该分支中程序为通过期中测试的程序

## 【问题描述】

请根据修改的文法，在词法分析、语法分析程序的基础上进行**增量式修改**，识别出各类单词和语法成分**（无需考虑错误处理）**。输入输出及处理要求如下：

（1）需按文法规则，用递归子程序法对文法中定义的语法成分进行分析；

（2）为了方便进行自动评测，输入的被编译源文件统一命名为 testfile.txt（注意不要写错文件名）；输出的结果文件统一命名为 output.txt（注意不要写错文件名）；结果文件中包含如下两种信息：

  1）按词法分析识别单词的顺序，按行输出每个单词的信息（要求同词法分析作业，对于预读的情况不能输出）。

  2）在文法中出现（除了\<BlockItem\>, \<Decl\>, \<BType\> 之外）的语法分析成分分析结束前，另起一行输出当前语法成分的名字，形如“\<Stmt\>”（注：未要求输出的语法成分仍需要进行分析，但无需输出）

  3）本次考核中修改了文法，要求同学们增加对 repeat ... until ... 、十六进制表示的整数常量进行【词法】和【语法】分析。具体新增的规则见下文给出的【文法】和【词法输出格式】的高亮部分。

## 【一、文法规则变化】



  编译单元 CompUnit → {Decl} {FuncDef} MainFuncDef 

  声明 Decl → ConstDecl | VarDecl

  常量声明 ConstDecl → 'const' BType ConstDef { ',' ConstDef } ';' 

  基本类型 BType → 'int'

  常数定义 ConstDef → Ident { '[' ConstExp ']' } '=' ConstInitVal 

  常量初值 ConstInitVal → ConstExp | '{' [ ConstInitVal { ',' ConstInitVal } ] '}'

  变量声明 VarDecl → BType VarDef { ',' VarDef } ';' 

  变量定义 VarDef → Ident { '[' ConstExp ']' } | Ident { '[' ConstExp ']' } '=' InitVal

  变量初值 InitVal → Exp | '{' [ InitVal { ',' InitVal } ] '}' 

  函数定义 FuncDef → FuncType Ident '(' [FuncFParams] ')' Block

  主函数定义 MainFuncDef → 'int' 'main' '(' ')' Block 

  函数类型 FuncType → 'void' | 'int'

  函数形参表 FuncFParams → FuncFParam { ',' FuncFParam } 

  函数形参 FuncFParam → BType Ident ['[' ']' { '[' ConstExp ']' }] 

  语句块 Block → '{' { BlockItem } '}' 

  语句块项 BlockItem → Decl | Stmt

  **语句 Stmt** → LVal '=' Exp ';'
    | [Exp] ';' 
    | Block 
    | 'if' '( Cond ')' Stmt [ 'else' Stmt ] 
    | 'while' '(' Cond ')' Stmt
    | 'break' ';' | 'continue' ';'
    | 'return' [Exp] ';'
    | LVal = 'getint''('')'';'
    | 'printf' '('FormatString {',' Exp} ')'';' 

​    **| 'repeat' Stmt 'until' '(' Cond ')' ';'** 

  表达式 Exp → AddExp 注：表达式是十进制或十六进制表示的 int 型表达式 

  条件表达式 Cond → LOrExp 

  左值表达式 LVal → Ident {'[' Exp ']'} 

  基本表达式 PrimaryExp → '(' Exp ')' | LVal | Number 

  **数值 Number → IntConst | HexadecimalConst**

  一元表达式 UnaryExp → PrimaryExp | Ident '(' [FuncRParams] ')' | UnaryOp UnaryExp 

  单目运算符 UnaryOp → '+' | '−' | '!' 注：'!'仅出现在条件表达式中 

  函数实参表 FuncRParams → Exp { ',' Exp } 

  乘除模表达式 MulExp → UnaryExp | MulExp ('*' | '/' | '%') UnaryExp 

  加减表达式 AddExp → MulExp | AddExp ('+' | '−') MulExp 

  关系表达式 RelExp → AddExp | RelExp ('<' | '>' | '<=' | '>=') AddExp 

  相等性表达式 EqExp → RelExp | EqExp ('==' | '!=') RelExp 

  逻辑与表达式 LAndExp → EqExp | LAndExp '&&' EqExp

  逻辑或表达式 LOrExp → LAndExp | LOrExp '||' LAndExp 

  常量表达式 ConstExp → AddExp 注：使用的Ident 必须是常量 

  （注：以下多条规则来源于文档中，只做说明，和原有语法分析一样，不需要将其作为语法成分输出！！！）

  格式化字符 FormatChar -> '%d'

  普通字符 NormalChar → 十进制编码为32,33,40-126的ASCII字符

  字符 Char → FormatChar | NormalChar

  格式化字符串 FormatString → '"'{ Char }'"'

**新增的十六进制部分**：



  **十六进制数常量 HexadecimalConst → HexadecimalPrefix HexadecimalDigit | HexadecimalConst HexadecimalDigit**

  **十六进制数前缀 HexadecimalPrefix → '0x' | '0X'**

  **十六进制数字 HexadecimalDigit → 以下字符之一：0 1 2 3 4 5 6 7 8 9 a b c d e f A B C D E F** 

## 【二、单词输出规则变化】



| 单词名称         | 类别码     | 单词名称 | 类别码   | 单词名称 | 类别码 | 单词名称 | 类别码  |
| ---------------- | ---------- | -------- | -------- | -------- | ------ | -------- | ------- |
| **Ident**        | IDENFR     | !        | NOT      | *        | MULT   | =        | ASSIGN  |
| **IntConst**     | INTCON     | &&       | AND      | /        | DIV    | ;        | SEMICN  |
| **FormatString** | STRCON     | \|\|     | OR       | %        | MOD    | ,        | COMMA   |
| main             | MAINTK     | while    | WHILETK  | <        | LSS    | (        | LPARENT |
| const            | CONSTTK    | getint   | GETINTTK | <=       | LEQ    | )        | RPARENT |
| int              | INTTK      | printf   | PRINTFTK | >        | GRE    | [        | LBRACK  |
| break            | BREAKTK    | return   | RETURNTK | >=       | GEQ    | ]        | RBRACK  |
| continue         | CONTINUETK | +        | PLUS     | ==       | EQL    | {        | LBRACE  |
| if               | IFTK       | -        | MINU     | !=       | NEQ    | }        | RBRACE  |
| else             | ELSETK     | void     | VOIDTK   |          |        |          |         |

在以上词法分析的输出格式的基础上，为了兼容本次期中考核所需考察的字符型变量/常量，新增了三种单词输出格式。

| repeat                       | REPEATTK |
| ---------------------------- | -------- |
| until                        | UNTILTK  |
| **HexConst(十六进制数常量)** | HEXCON   |



【输出形式】按如上要求将语法分析结果输出至output.txt中。

【输入形式】testfile.txt中的符合文法要求的测试程序。

【样例输入】

```
const int CONV = 0xfff;

int func() {
   return 0X86ab;
}

int main()
{
    int PI = CONV + func();
      
    repeat{
        PI = PI + 1;
    
    }until(PI > 100);
      
    return 0;
}
```

【样例输出】

```
CONSTTK const
INTTK int
IDENFR CONV
ASSIGN =
HEXCON 0xfff
<Number>
<PrimaryExp>
<UnaryExp>
<MulExp>
<AddExp>
<ConstExp>
<ConstInitVal>
<ConstDef>
SEMICN ;
<ConstDecl>
INTTK int
<FuncType>
IDENFR func
LPARENT (
RPARENT )
LBRACE {
RETURNTK return
HEXCON 0X86ab
<Number>
<PrimaryExp>
<UnaryExp>
<MulExp>
<AddExp>
<Exp>
SEMICN ;
<Stmt>
RBRACE }
<Block>
<FuncDef>
INTTK int
MAINTK main
LPARENT (
RPARENT )
LBRACE {
INTTK int
IDENFR PI
ASSIGN =
IDENFR CONV
<LVal>
<PrimaryExp>
<UnaryExp>
<MulExp>
<AddExp>
PLUS +
IDENFR func
LPARENT (
RPARENT )
<UnaryExp>
<MulExp>
<AddExp>
<Exp>
<InitVal>
<VarDef>
SEMICN ;
<VarDecl>
REPEATTK repeat
LBRACE {
IDENFR PI
<LVal>
ASSIGN =
IDENFR PI
<LVal>
<PrimaryExp>
<UnaryExp>
<MulExp>
<AddExp>
PLUS +
INTCON 1
<Number>
<PrimaryExp>
<UnaryExp>
<MulExp>
<AddExp>
<Exp>
SEMICN ;
<Stmt>
RBRACE }
<Block>
<Stmt>
UNTILTK until
LPARENT (
IDENFR PI
<LVal>
<PrimaryExp>
<UnaryExp>
<MulExp>
<AddExp>
<RelExp>
GRE >
INTCON 100
<Number>
<PrimaryExp>
<UnaryExp>
<MulExp>
<AddExp>
<RelExp>
<EqExp>
<LAndExp>
<LOrExp>
<Cond>
RPARENT )
SEMICN ;
<Stmt>
RETURNTK return
INTCON 0
<Number>
<PrimaryExp>
<UnaryExp>
<MulExp>
<AddExp>
<Exp>
SEMICN ;
<Stmt>
RBRACE }
<Block>
<MainFuncDef>
<CompUnit>
```

【评分标准】按与预期结果不一致的行数扣分，每项扣5%。

【开发语言及环境】用 C/C++/JAVA 实现，机房安装的 C/C++ 开发环境是 CLion 2018.3.4 和 CodeBlocks 20.03；Java 的开发环境为 IDEA 2018.3.6 社区版。平台支持 C++11 标准。评测机所采用的编译学生代码的版本是：C/C++ gcc 8.1.0，Java jdk 1.8。

【提交形式】将所开发的语法分析程序的源文件（.cpp/.c/.h/.java，不含工程文件）打包为zip或rar后提交。对于使用 java 开发的编译器，程序运行的入口为 src 目录下 Compiler.java 中的 main 方法。上传请直接打包 src 文件夹，如果引用了第三方外部包（不推荐），请将外部 jar 包文件放到 bin 目录下，bin 和 src 两个文件夹同级，将 bin 和 src 一起打包后提交即可。注意 mac 压缩会产生额外的文件到压缩包中，需删掉额外文件后提交。

【测试样例说明及提示】

  本次考核共有10个测试点，均 Accept 即可拿到满分。

| 测试点     | 说明                        |
| ---------- | --------------------------- |
| testfile1  | 不涉及新增文法              |
| testfile2  |                             |
| testfile3  |                             |
| testfile4  | 仅增加repeat...until...内容 |
| testfile5  |                             |
| testfile6  | 仅增加HexConst内容          |
| testfile7  |                             |
| testfile8  | 包含全部内容                |
| testfile9  |                             |
| testfile10 |                             |