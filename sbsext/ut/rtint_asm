* Put integer onto RI stack and return  V0.1    1985  Tony Tebby   QJUMP
*
        section utils
*
        xdef    ut_rtint                return integer d1
        xdef    ut_retin                return integer type
*
        xref    ut_ckri6                check for room
        xref    ut_reta1                return (a1)
*
ut_rtint
        jsr     ut_ckri6                check for space
        subq.l  #2,a1
        move.w  d1,(a6,a1.l)            copy the integer
ut_retin
        moveq   #3,d4                   set integer type
        bra.s   ut_reta1
        end
