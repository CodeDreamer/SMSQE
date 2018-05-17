; Simple choice box                      1991 Jochen Merz

        section utility


        include dev8_keys_thg
        include  dev8_mac_xref
        include  dev8_keys_menu

        xdef    mu_selct
        
;+++
; Select one out of two or three choices.
;               Entry                   Exit
;       D0.l                            error, 0 or +1 for ESC
;       D1.l    origin or -1            preserved
;       D2.l    colourways              preserved
;       D3.l                            option selected
;       D5.l    ptr to 1st choice       smashed
;       D6.l    ptr to 2nd choice       smashed
;       D7.l    ptr to 3rd choice or 0  smashed
;       A0      window title            preserved
;       A2      window message          preserved
;---
mu_selct
stk.frm equ     $44
slc_reg reg     a0-a2/a4-a5
        movem.l slc_reg,-(sp)
        sub.l   #stk.frm,sp
        move.l  sp,a5                   ; prepare workspace

        movem.l d1-d2,-(sp)
        move.l  #'ITSL',d2
        xbsr    ut_usmen
        movem.l (sp)+,d1-d2
        bne.s   slc_error               ; cannot use menu

        move.l  a1,a4                   ; that's the Thing

        move.l  #(thp.call+thp.str)<<16,d3

        move.l  d3,is_mentp(a5)
        move.l  a0,is_mennm(a5)         ; window title

        move.l  d3,is_prmtp(a5)
        move.l  a2,is_prmpt(a5)         ; request string

        move.l  d3,is_item1-4(a5)
        move.l  d5,is_item1(a5)         ; option 1

        move.l  d3,is_item2-4(a5)
        move.l  d6,is_item2(a5)         ; option 2

        move.l  d3,is_item3-4(a5)
        move.l  d7,is_item3(a5)         ; option 3

        swap    d1
        move.l  d1,is_xpos(a5)          ; x-origin

        swap    d1
        move.l  d1,is_ypos(a5)          ; y-origin

        move.l  d2,is_mainc(a5)         ; main colourway

        move.l  #(thp.ret+thp.ulng)<<16,is_itnum-4(a5)
        lea     $40(a5),a0
        move.l  a0,is_itnum(a5)         ; return parameter

        move.l  a5,a1                   ; the parameter table
        jsr     thh_code(a4)            ; do request via menu
        bne.s   err_rts                 ; failed, do not read return parameter

        move.l  $40(a5),d3              ; return parameter

err_rts
        xbsr    ut_frmen                ; free menu
slc_error
        add.l   #stk.frm,sp             ; adjust stack
        movem.l (sp)+,slc_reg
        tst.l   d0
        rts

        end
