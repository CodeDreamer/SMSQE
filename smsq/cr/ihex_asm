* Integer to hexadecimal string conversion   V2.00    1986  Tony Tebby   QJUMP
*
        section cv
*
        xdef    cr_ibhex        Integer byte to hexadecimal
        xdef    cr_iwhex                word
        xdef    cr_ilhex                long
        xdef    cr_ihex
*
*       d0  r   0 no error possible
*       d0 cr   number of characters to convert / 0 no error (cr_ihex)
*       d1  r   (long) value to convert
*       d1 c  p (long) value to convert (cr_ihex)
*       d2   sp digit value
*       a0 c  u pointer to buffer to put characters
*       a1 c  u pointer to stack to get value from (cr_i.hex)
*       a6      base address
*
cr_ibhex
        moveq   #0,d1                   set value to convert
        move.b  (a6,a1.l),d1
        addq.l  #1,a1
        moveq   #2,d0                   convert 2 digits
        bra.s   cr_ihex
*
cr_iwhex
        moveq   #0,d1                   set value to convert
        move.w  (a6,a1.l),d1
        addq.l  #2,a1 
        moveq   #4,d0                   convert 4 digits
        bra.s   cr_ihex
*
cr_ilhex
        move.l  (a6,a1.l),d1            set value to convert
        addq.l  #4,a1
        moveq   #8,d0                   convert 8 digits
*
cr_ihex
        add.l   d0,a0                   new end
        movem.l a0/d1/d2,-(sp)          save registers used
        bra.s   cih_eloop
*
cih_loop
        moveq   #$f,d2                  mask out one digit
        and.b   d1,d2
        lsr.l   #4,d1                   next digit
        add.b   #'0',d2                 make ascii
        cmp.b   #'9',d2                 digit?
        ble.s   cih_put                 ... yes
        addq.b  #'A'-$a-'0',d2          make it a letter
cih_put
        subq.l  #1,a0
        move.b  d2,(a6,a0.l)            set character
cih_eloop
        dbra    d0,cih_loop
*
        moveq   #0,d0                   no error
        movem.l (sp)+,a0/d1/d2          restore regs
        rts
        end
