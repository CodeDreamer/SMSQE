* Error processing extras  V0.1    1985  Tony Tebby   QJUMP
*
        section exten
*
*       ERR_DF                          true if last error was drive full
*
        xdef    err_df
*
        xref    ut_par0                 check for no parameters
        xref    ut_rtint                return integer
*
        include dev8_sbsext_ext_keys
*
err_df
        moveq   #err.df,d1              compare with err.df
        bsr.l   ut_par0                 check for no parameters
        sub.l   bv_ernum(a6),d1
        seq     d1                      equ=255
        and.w   #1,d1
        bra.l   ut_rtint                and return it
        end
