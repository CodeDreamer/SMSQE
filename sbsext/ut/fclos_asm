* Close file    V0.05    1985  Tony Tebby  QJUMP
*
        section utils
*
        xdef    ut_fclos                close file
        xdef    ut_fceof                close file eof is OK
        xdef    ut_eofok                set eof to OK
        xdef    ut_ok
*
        include dev8_sbsext_ext_keys
*
ut_fceof
        bsr.s   ut_eofok                set end of file to ok
ut_fclos
        move.l  d0,-(sp)                save error code
        moveq   #io.close,d0            close file
        trap    #2
        move.l  (sp)+,d0
        rts
*
ut_eofok
        moveq   #err.ef,d4              test for eof
        cmp.l   d0,d4                   is it?
        bne.s   utfc_rts                ... no, leave it
ut_ok
        moveq   #0,d0                   ... yes, clear it
utfc_rts
        rts
        end
