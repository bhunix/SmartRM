#include<stdio.h>
#include<stdlib.h>
#include<string.h>
#include<stdint.h>
typedef struct stu
{
    struct stu *a;
    char b;
}_stu;

#define print(_buf, _n) do {\
    int i; \
    for (i=0;i<_n;i++) {\
        printf("%d\n", _buf[i]);\
    }\
}while (0)\

main(){
    struct stu *stu = NULL;
    stu = (struct stu *)malloc(sizeof(struct stu));
    free(stu);
}

compare(struct stu *stu)
{
    printf("%c\n", stu->b);
}

test(char *c)
{
    uint8_t a[2];
    uint32_t s = 23;
    ((unsigned char*)a)[0] = (unsigned char)(((s)) & 0xff);
    ((unsigned char*)a)[1] = (unsigned char)((s>>8) & 0xff);

    printf("%x\n", (s>>8) & 0xff);

}

