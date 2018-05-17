; Check for hit on PAN and SCROLL bars  V1.00    1989  Tony Tebby  QJUMP

        section wman

        xdef    wm_htbar

        include 'dev8_keys_err'
        include 'dev8_keys_k'
        include 'dev8_keys_qdos_pt'
        include 'dev8_keys_qdos_io'
        include 'dev8_keys_wwork'
        include 'dev8_keys_wstatus'
;+++
;       d0  r   condition return (>0 if event = item number)
;       d1 c  p pointer position relative to main window
;       d2  r   item number
;       d3  r   position of hit
;       d4 cr   0 or pt..do / pt..pan or pt..scrl
;       a1 c  p pointer to status area
;       a3  r   pointer to sub-window definition
;       a4 c  p pointer to working definition
;
;               all other registers preserved
;+++
wm_htbar
whb.reg reg     d1/d5/d6/d7/a0/a1/a2
        move.l  ww_pappl(a4),d0          ; application sub-window list
        beq.s   whb_rts
        tst.b   d4                       ; do?
        bne.s   whb_do                   ; ... yes
        cmp.b   #k.hit,wsp_kprs(a1)      ; can only drag on hit
        beq.s   whb_do                   ; ... yes
        moveq   #0,d0
whb_rts
        rts

whb_do
        movem.l whb.reg,-(sp)
        move.l  d0,a1

whb_wloop
        move.l  (a1)+,d0                 ; next window
        beq.l   whb_exit
        move.l  d0,a3
        tst.l   wwa_ctrl(a3)             ; controlled?
        beq.s   whb_wloop                ; ... no

        move.l  wwa_part(a3),d0          ; any x control?
        beq.s   whb_doy                  ; ... no
        move.l  d0,a2
        move.w  (a2)+,d6                 ; how many sections?
        ble.s   whb_doy                  ; ... none

        tst.l   wwa_psbc(a3)             ; colours
        beq.s   whb_doy                  ; ... none

        move.l  d1,d7

        sub.w   wwa_yorg(a3),d7
        sub.w   wwa_ysiz(a3),d7
        ble.s   whb_doy                  ; ... no
        cmp.w   #ww.pnbar+1,d7           ; outside bar?
        bgt.s   whb_doy
        swap    d7
        sub.w   wwa_xorg(a3),d7          ; in bar?
        blt.s   whb_doy
        move.w  wwa_xsiz(a3),d5
        cmp.w   d5,d7
        bge.s   whb_doy

        move.l  ww_wstat(a4),a1
        move.l  ws_ptpos(a1),d1          ; absolute pointer position
        swap    d1
        move.w  ww_xorg(a4),d1           ; xmin
        add.w   wwa_xorg(a3),d1
        move.l  d1,d2
        add.w   wwa_xsiz(a3),d2          ; xmax
        subq.w  #1,d2
        swap    d1
        swap    d2
        bsr.l   whb_splm                 ; set the limits

        moveq   #pt..pan,d0              ; preset pan
        move.l  #wsi.pan<<8,d2
        bra.s   whb_fsect

whb_doy
        move.l  wwa_part+wwa.clen(a3),d0 ; any x control?
        beq.s   whb_wloop                ; ... no
        move.l  d0,a2
        move.w  (a2)+,d6                 ; how many sections?
        ble.s   whb_wloop                ; ... none

        tst.l   wwa_psbc+wwa.clen(a3)    ; colours
        beq.s   whb_wloop                ; ... none

        move.l  d1,d7

        swap    d7
        sub.w   wwa_xorg(a3),d7
        sub.w   wwa_xsiz(a3),d7          ; off bottom of window?
        ble.l   whb_wloop                ; ... no
        cmp.w   #ww.scbar+2,d7           ; outside bar?
        bgt.l   whb_wloop
        swap    d7
        sub.w   wwa_yorg(a3),d7          ; in bar?
        blt.l   whb_wloop
        move.w  wwa_ysiz(a3),d5
        cmp.w   d5,d7
        bge.l   whb_wloop

        move.l  ww_wstat(a4),a1
        move.l  ws_ptpos(a1),d1          ; absolute pointer position
        move.w  ww_yorg(a4),d1           ; ymin
        add.w   wwa_yorg(a3),d1
        move.l  d1,d2
        add.w   wwa_ysiz(a3),d2          ; ymax
        subq.w  #1,d2
        bsr.s   whb_splm                 ; set the limits

        moveq   #pt..scrl,d0             ; preset scroll
        move.l  #wsi.scr<<8,d2

whb_fsect
        moveq   #0,d3                    ; start position

        tst.b   d4                       ; event?
        beq.s   whb_sloop                ; ... no
        bset    #wsi..xt,d2              ; ... yes

whb_sloop
        addq.b  #1,d2
        add.w   (a2),d3                  ; start pixel position
        addq.l  #6,a2                    ; next lump
        move.w  d5,d4                    ; end pixel position
        cmp.b   d6,d2                    ; last section?
        beq.s   whb_spos                 ; ... yes
        move.w  (a2),d4                  ; ... no, end position
        subq.w  #1,d4
whb_spos
        cmp.w   d4,d7
        bge.s   whb_snext
        sub.w   d3,d7                    ; position in section
        bge.s   whb_set                  ; ... in section
        btst    #wsi..xt,d2              ; is join allowed?
        beq.s   whb_ok                   ; ... no, ignore it
whb_set
        subq.b  #1,d2                    ; set item

        sub.w   d3,d4                    ; size of section
        move.w  d7,d3
        swap    d3
        move.w  d4,d3                    ; position of hit

        move.l  d0,d4                    ; set event

        move.l  d2,d0                    ; set cond code
        bra.s   whb_exit

whb_snext
        moveq   #1,d3                    ; next section start
        cmp.b   d6,d2                    ; all sections done?
        bne.s   whb_sloop
whb_ok
        moveq   #0,d0
whb_exit
        movem.l (sp)+,whb.reg
        rts

whb_splm
        moveq   #iop.splm,d0             ; set pointer limits
        move.l  ww_chid(a4),a0
        moveq   #forever,d3
        trap    #do.io
        rts
        end
