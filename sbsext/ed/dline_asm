* ED - delete a line      1985  Tony Tebby    QJUMP
*
        section ed
*
        xdef    ed_dline
        xdef    ed_dscr
*
        xref    ed_snorm
        xref    ed_delln
        xref    ed_rewrt
        xref    edw_d5
*
        include dev8_sbsext_ed_keydef
        include dev8_sbsext_ed_data
*
ed_dline
        bsr.l   ed_snorm
        move.w  edr_lnr(a5,d5.w),d4     get line number
        ble.s   eddl_scr                ... not a line at all
        bsr.l   ed_delln                and delete from file
ed_dscr
eddl_scr
        moveq   #0,d4                   say no line left on screen
        bsr.s   ed_rewrt                and rewrite the non existant line
        cmp.b   #k.down,ed_ccmd(a5)     is it cursor down command
        beq.s   eddl_exit               ... yes, done
        addq.w  #1,d6                   move back down
eddl_exit
        bra.l   edw_d5                  setting d5
        end
