* Integer to binary string conversion   V2.00    1986  Tony Tebby   QJUMP
*
        section cv
*
        xdef    cr_ibbin        Integer byte to binary
        xdef    cr_iwbin                word
        xdef    cr_ilbin                long
        xdef    cr_ibin
*
*       d0  r   0 no error possible
*       d0 cr   value to convert / 0 (cr_ibin)
*       d1  r   (long) nr characters converted
*       d1 cr   (long) number of characters to convert / nr converted (cr_ibin)
*       d2   sp digit value
*       a0 c  u pointer to buffer to put characters
*       a1 c  u pointer to stack to get value from (cr_i.bin)
*       a6      base address
*
cr_ibbin
        moveq   #0,d0                   set value to convert
        move.b  (a6,a1.l),d0
        addq.l  #1,a1
        moveq   #8,d1                   convert 8 digits
        bra.s   cr_ibin
*
cr_iwbin
        moveq   #0,d0                   set value to convert
        move.w  (a6,a1.l),d0
        addq.l  #2,a1 
        moveq   #16,d1                  convert 16 digits
        bra.s   cr_ibin
*
cr_ilbin
        move.l  (a6,a1.l),d0            set value to convert
        addq.l  #4,a1
        moveq   #32,d1                  convert 32 digits
*
cr_ibin
        add.l   d1,a0                   new end
        movem.l a0/d1/d2,-(sp)          save registers used
        bra.s   cib_eloop
*
cib_loop
        moveq   #$18,d2                 1/2 of '0'
        roxr.l  #1,d0                   next bit
        roxl.b  #1,d2                   into character
        subq.l  #1,a0
        move.b  d2,(a6,a0.l)            set character
cib_eloop
        dbra    d1,cib_loop
*
        moveq   #0,d0                   no error
        movem.l (sp)+,a0/d1/d2          restore regs
        rts
        end
