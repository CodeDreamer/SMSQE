; Procedure to list HOTKEYs       1988   Tony Tebby   QJUMP

        section hotkey

        xdef    hot_list

        xref    gu_achp0
        xref    gu_rchp
        xref    ut_winset
        xref    ut_winchk
        xref    hot_thus
        xref    hot_thfr
        xref    hot_gnam

        xref    hk_fitmc

        xref    cv_cttab
        xref    cv_vchar
        xref    cv_vchrs

        xref    met_spce

        include 'dev8_keys_qdos_io'
        include 'dev8_keys_k'
        include 'dev8_ee_hk_data'

bv_buffr equ    0
;+++ 
; List Hotkey names
;
; HOT_LIST #channel|\file
;---
hot_list
        moveq   #$10,d0                  ; little heap
        jsr     gu_achp0                 ; allocate
        bne.l   hl_exit
        move.l  a0,d7
        jsr     ut_winset                ; get channel ID and set
        bne.l   hl_rchp7
        cmp.l   a3,a5                    ; and nothing else
        bne.l   hl_rchp7

        move.l  d7,-(sp)
        move.l  bv_buffr(a6),a1          ; point to buffer
        move.w  (a6,a1.l),d7             ; window width

hl_gtall
        moveq   #0,d4                    ; hotkey

        jsr     hot_thus                 ; find hotkey linkage
        bne.l   hl_rchp

hl_nloop
        move.w  d4,d1
        jsr     hk_fitmc                 ; find item with given case
        bne.l   hl_retry                 ; ... none
        jsr     hot_gnam                 ; get name
        bne.l   hl_retry                 ; none
        lea     (a4,d1.w),a5             ; name in a4-a5
        jsr     hot_thfr                 ; free hotkey

        move.w  d4,d1                    ; write out character
        swap    d4
        move.w  hki_type(a1),d4          ; keep type
        bclr    #hki..trn,d4             ; ... ignoring transient flag
        lea     met_spce,a2
        move.l  (sp),a1
        jsr     cv_vchrs

        clr.w   d5                       ; space taken
        jsr     ut_winchk                ; check window
        bsr.l   hl_sstrg                 ; send key
        bne.s   hl_rchp
        bsr.l   hl_pad1

        lea     hot_ttab(pc),a1          ; type table
        add.w   d4,a1
        add.w   (a1),a1
        bsr.s   hl_sstrg                 ; send type
        bra.s   hl_ploop

hl_winchk
        clr.w   d5
        jsr     ut_winchk                ; check next line
        bsr.s   hl_pad                   ; and some extra spaces
        bsr.s   hl_pad

hl_ploop
        bsr.s   hl_pad                   ; and pad
hl_cloop
        cmp.l   a5,a4
        bge.s   hl_nnext
        move.l  (sp),a1
        move.b  (a4)+,d1
        cmp.b   #k.enter,d1              ; enter?
        beq.s   hl_newl
        jsr     cv_vchar                 ; make visible
        beq.s   hl_stnd                  ; standard
        tst.b   d4                       ; was previous a normal character?
        beq.s   hl_ckspc                 ; ... no
        bsr.s   hl_pad
hl_ckspc
        tst.l   d6                       ; console?
        beq.s   hl_wrspc                 ; ... no
        moveq   #iow.sula,d0             ; underline
        moveq   #-1,d1
        bsr.s   hl_io
        move.l  (sp),a1
        bsr.s   hl_sstrg                 ; send string
        moveq   #iow.sula,d0             ; underline off
        moveq   #0,d1
        bsr.s   hl_io
        bra.s   hl_ploop

hl_wrspc
        move.l  (sp),a1                  ; send special to non console
        bsr.s   hl_sstrg
        bra.s   hl_ploop

hl_stnd
        st      d4
        bsr.s   hl_sstrg
        bra.s   hl_cloop

hl_newl
        bsr.s   hl_sbyte                 ; send newline
        clr.w   (a1)
        bra.s   hl_winchk                ; and next line

hl_nnext
        moveq   #k.enter,d1              ; end of line
        bsr.s   hl_sbyte
        swap    d4

        jsr     hot_thus                 ; get thing back
        bne.s   hl_rchp
hl_retry
        moveq   #0,d0                    ; ok now
        addq.b  #$1,d4                   ; next key
        bne.l   hl_nloop

        jsr     hot_thfr                 ; free hotkey
hl_rchp
        move.l  (sp)+,d7                 ; and return our bit of heap
hl_rchp7
        move.l  d7,a0
        jsr     gu_rchp
hl_exit
        rts

hl_sstrg
        moveq   #iob.smul,d0
        move.w  (a1)+,d2                 ; number of characters
        add.w   d2,d5                    ; new position
        cmp.w   d7,d5
        ble.s   hl_io

        move.w  d7,d5                    ; at end
        moveq   #0,d0
        rts

hl_pad
        clr.w   d4                       ; no funny character
hl_pad1
        bsr.s   hl_sspc                  ; send spaces
        addq.w  #1,d5
        cmp.w   #8,d5                    ; to column 8
        blt.s   hl_pad1
        bra.s   hl_rts

hl_sspc
        moveq   #' ',d1
hl_sbyte
        moveq   #iob.sbyt,d0
hl_io
        moveq   #forever,d3
        trap    #do.io
hl_rts
        tst.l   d0
        rts
        page

        xref    met_key
        xref    met_cmd
        xref    met_exec
        xref    met_load
        xref    met_pick
        xref    met_wake

        dc.w    met_key-*
        dc.w    met_key-*
        dc.w    met_key-*
        dc.w    met_key-*
hot_ttab
        dc.w    met_cmd-*
        dc.w    met_exec-*
        dc.w    met_exec-*
        dc.w    met_load-*
        dc.w    met_pick-*
        dc.w    met_wake-*
        dc.w    met_wake-*
        end
