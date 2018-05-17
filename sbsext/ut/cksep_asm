* Check separators      V0.5    1984  Tony Tebby   QJUMP
*
* check if parameter is preceded by a given separator
*
        section utils
*
        xdef    ut_ckcomma
        xdef    ut_ckto
        xdef    ut_cksemi
        xdef    ut_cksep
*
        include dev8_sbsext_ext_keys
        include dev8_sbsext_ext_ex_defs
*
ut_ckcomma
        moveq   #%00010000,d0   check for comma
        bra.s   ut_cksep        (hash permitted)
ut_ckto
        moveq   #%01010000,d0   check for to
        bra.s   ut_cksep
ut_cksemi
        moveq   #%00100000,d0   check for semicolon
        tst.b   1(a6,a3.l)      is it preceded by a #?
        bmi.s   ut_ckbp         ... yes
ut_cksep
        cmp.l   parm_st+4(sp),a3
        ble.s   ut_ckbp         it is the first parameter, preceded by no sep
        moveq   #%01110000,d1   mask out all but the separator
        and.b   -7(a6,a3.l),d1  ... from previous entry
        sub.b   d1,d0           and set d0 to 0 if equal
        beq.s   ut_ckrts
ut_ckbp
        moveq   #err.bp,d0      otherwise err.bp
ut_ckrts
        rts
        end
