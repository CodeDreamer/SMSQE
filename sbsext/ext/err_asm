* Error processing extras  V0.1    1985  Tony Tebby   QJUMP
*
        section exten
*
*       REPORT [#channel,][ernum]       report an error
*
        xdef    report
*
        xref    ut_chan0                get channel default d0
        xref    ut_gtlin                get long integer
        xref    ut_errms                write error message
*
        include dev8_sbsext_ext_keys
*
report
        bsr.l   ut_chan0                get channel
        bne.s   err_rts
        bsr.l   ut_gtlin                get a long integer
        bne.s   err_rts
        move.l  bv_ernum(a6),d0         get last error
        subq.w  #1,d3                   a parameter?
        blt.s   err_write               ... no write last
        bgt.s   err_bp
        move.l  (a6,a1.l),d0            set error
err_write
        bra.l   ut_errms                write error message
err_bp
        moveq   #err.bp,d0
err_rts
        rts
        end
