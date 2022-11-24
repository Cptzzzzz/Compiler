#include <stdio.h>

int main(){
    char c;
    int num=0;
    while((c=getchar())!=EOF){
    if(num>=2) putchar(c);
        if(c=='\n'){
        num++;}
    }
}