* Float a long word in D1 onto the RI stack  V0.2   1984  Tony Tebby  QJUMP
* assumes enough space already there
*
        section utils
*
        xdef    ut_lfla1
        xdef    ut_lfloat
        xdef    ut_rnorm
*
        include dev8_sbsext_ext_keys
*
ut_lfla1
        move.l  bv_rip(a6),a1   set A1 to RI stack pointer
*
ut_lfloat
        subq.l  #6,a1           put
        clr.w   (a6,a1.l)       zero exponent on
        tst.l   d1
        beq.s   lfl_mant        ... and zero mantissa
        move.w  #$0820,d2       and set unnormalised exponent (+1)
lfl_norm
        subq.w  #1,d2           reduce exponent
ut_rnorm
        asl.l   #1,d1           and multiply mantissa by 2
        bvc.s   lfl_norm        if not overflowed yet, try again
        roxr.l  #1,d1           restore mantissa to non overflowed
        move.w  d2,(a6,a1.l)    put actual exponent on ri stack
lfl_mant
        move.l  d1,2(a6,a1.l)   and mantissa
        rts
        end
