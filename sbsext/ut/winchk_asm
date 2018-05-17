* Window control  V0.5    1984  Tony Tebby   QJUMP
*
        section utils
*
        xdef    ut_winset               set up window check in d6
        xdef    ut_winchk               check for room in window
*
        xref    ut_impch                open implicit channel or get #n or defau
        xref    ut_wrtnl                write a newline
        xref    ut_trp3r                trap #3 (relative)
*
        include dev8_sbsext_ext_keys
*
*       d6      window control register, set by winset, used by winchk
*       a0 cr   channel id, set by winset, preserved by winchk
*
ut_winset
        moveq   #io.new,d3              open implicit new
        moveq   #1,d6                   or default #1
        bsr.l   ut_impch                find channel id
        bne.s   utw_rts                 ... oops
        moveq   #0,d6                   set infinite length
        moveq   #sd.chenq,d0            find window size
        move.l  bv_bfbas(a6),a1
        move.w  #80,(a6,a1.l)           default to 80 cols
        bsr.l   ut_trp3r
        bne.s   utw_ok
        move.l  bv_bfbas(a6),a1
        move.w  2(a6,a1.l),d6           nr lines in d6
        swap    d6                      top end
        bsr.l   ut_wrtnl                write a blank line
utw_ok
        moveq   #0,d0
utw_rts
        rts
*
ut_winchk
        move.l  d6,d0                   get page length 
        beq.s   utw_rts
        swap    d0
        addq.w  #1,d6                   and update line count
        cmp.w   d6,d0
        bne.s   utw_rts                 ... not at end
        clr.w   d6                      ... at end reset
        move.l  a0,-(sp)
        move.l  d2,-(sp)
        moveq   #mt.inf,d0              find system variables
        trap    #1
        st      sv_scrst(a0)            freeze screen
        move.l  (sp)+,d2                restore count
        move.l  (sp)+,a0                restore channel id
        rts
        end
