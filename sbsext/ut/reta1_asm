* Return item on the arithmetic stack  V0.2    1985  Tony Tebby  QJUMP
*
        section utils
*
        xdef    ut_retst
        xdef    ut_rtfd1
        xdef    ut_retfp
        xdef    ut_reta1

        xref    ut_ckri6
        xref    ut_lfloat
*
        include dev8_sbsext_ext_keys
*
* ut_retst must be first  (falls through from ut_rtstr)
*
ut_retst
        moveq  #1,d4
        bra.s  ut_reta1
ut_rtfd1
        bsr.s  ut_ckri6          ; check room for 6 bytes
        bsr.s  ut_lfloat         ; float d1
ut_retfp
        moveq  #2,d4
ut_reta1
        move.l a1,bv_rip(a6)
        moveq  #0,d0
        rts
        end
