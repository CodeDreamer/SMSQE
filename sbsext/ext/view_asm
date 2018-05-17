* View a file  V0.7     1985  Tony Tebby  QJUMP
*
*       VIEW [#n],name                  view a file
*
        section exten
*
        xdef    view
* 
        xref    ut_winset       set up window
        xref    ut_winchk       check if in window
        xref    ut_fopin        open input file
        xref    ut_fceof        close file (eof is ok)
        xref    ut_trp3r        trap #3 relative
*
        include dev8_sbsext_ext_keys
*
view
        bsr.l   ut_winset               set window (and channel)
        bne.s   view_rts
        move.l  a0,a4                   save channel
        move.w  (a6,a1.l),d5            get window width
        bsr.l   ut_fopin                open input file
        bne.s   view_rts
*
view_loop
        moveq   #io.fline,d0            get a line
        move.w  d5,d2                   up to window width
        bsr.s   view_tr3
        exg     d1,d2                   save nr chars read
        beq.s   view_out                ... ok
        move.b  #$a,(a6,a1.l)           ... not ok, add nl to end
        addq.w  #1,d2
view_skip
        moveq   #io.fbyte,d0            read bytes
        bsr.s   view_tr3
        bne.s   view_close              until error
        cmp.b   #$a,d1                  or newline
        bne.s   view_skip
view_out
        bsr.l   ut_winchk               check if window full
        moveq   #io.sstrg,d0            send string
        exg     a0,a4
        bsr.s   view_tr3
        exg     a0,a4
        beq.s   view_loop
view_close
        bra.l   ut_fceof                close file (eof is ok)
*
view_tr3
        move.l  bv_bfbas(a6),a1         use basic buffer
        bra.l   ut_trp3r                and trap #3 relative
view_rts
        rts
        end
