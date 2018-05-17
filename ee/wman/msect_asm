; Find section of menu, check for PAN/SCROLL  V1.02   1986  Tony Tebby  QJUMP

        section wman

        xdef    wm_msect

        xref    wm_altps
        xref    wm_htpns 
        xref    wm_drins
        xref    wm_clrci

        include dev8_keys_k
        include dev8_keys_qdos_pt
        include dev8_keys_wwork
        include dev8_keys_wstatus

;       d0  r   condition return (0 or item number)
;       d1 c  p pointer position
;       d2 c  p upper cased keystroke
;       d3  r   section x,y (-1 if in arrows)
;       d4 cr   event number of keystroke
;       a0 c  p channel ID of window
;       a3 c  p pointer to sub-window definition
;       a4 c  p pointer to window working definition

;               all other registers preserved

reglist reg     d1/d2/d4/d5/a1/a2
stk_key  equ    $07
stk_d4   equ    $08

wm_msect
        moveq   #0,d3                    ; 0,0 in main section
        move.l  wwa_part+wwa.clen(a3),d0 ; any y control block?
        beq.s   wms_xctrl                ; ... no
        exg     d0,a2
        tst.w   (a2)                     ; any sections?
        exg     d0,a2
        bne.s   wms_do                   ; ... yes

wms_xctrl
        move.l  wwa_part(a3),d0          ; any x control block?
        beq.s   wms_rts0                 ; ... no
        exg     d0,a2
        tst.w   (a2)                     ; any sections?
        exg     d0,a2
        bne.s   wms_do                   ; ... yes
        moveq   #0,d0
wms_rts0
        rts                              ; cop out

wms_do
        movem.l reglist,-(sp) 
        moveq   #0,d2                    ; not in arrow row

        move.l  ww_wstat(a4),a1          ; pointer to status area
        sub.l   wsp_xorg(a1),d1          ; relative pointer position

        move.w  ws_citem(a1),d0          ; any current item?
        bmi.s   wms_chit                 ; ... no, check for new item
        cmp.w   #wsi.ctrl,d0             ; sub-window control item?
        bhs.s   wms_ikey                 ; ... yes, check for item keystroke

wms_chit              ; check hit areas

        jsr     wm_htpns                 ; in panscroll?
        beq.s   wms_ckey                 ; ... no, no item
        add.l   wsp_xorg(a1),d5          ; set absolute origin
        move.w  d0,ws_citem(a1)          ; set item number
        movem.l d4/d5,ws_cihit(a1)       ; reset hit area
        move.l  wwa_curw+wwa_iatt(a3),ws_cibrw(a1) ; set border width and colour
        lea     wm_drins,a2
        move.l  a2,ws_ciact(a1)
        jsr     (a2)                     ; draw it
        move.w  wwa_papr+wwa_watt(a3),ws_cipap(a1) ; and reset colour
        move.w  ws_citem(a1),d0          ; recover item 

wms_ikey                 ; check for key in control item
        moveq   #0,d3                    ; main section
        move.b  d0,d3                    ; actual scroll / pan section
        btst    #wsi..pan,d0             ; pan?
        beq.s   wms_ctrl
        swap    d3                       ; ... yes, section is x
wms_ctrl
        bset    #31,d2                   ; in control item
        move.b  wsp_kprs(a1),d4          ; key pressed?
        beq.s   wms_center               ; ... no, check enter
        cmp.b   #k.do,d4                 ; is hit/do pressed?
        blo.s   wms_seve                 ; hit
        beq.s   wms_sxtra                ; do

wms_center
        move.b  wsp_kstr(a1),d2          ; try keystroke
        cmp.b   #k.enter,d2              ; ENTER?
        beq.s   wms_sxtra                ; ... yes
        cmp.b   #k.space,d2              ; SPACE?
        beq.s   wms_seve                 ; ... yes

wms_ckey
        move.b  wsp_kstr(a1),d2          ; keystroke
        beq.s   wms_ok                   ; ... none
        cmp.b   #$ff,d2                  ; select window?
        beq.s   wms_ok                   ; yes, treat as none

        moveq   #k.curmk+k.ctrl+k.alt,d1 ; check for alt (not ctrl) cursor key
        and.b   d2,d1
        cmp.b   #k.curs+k.alt,d1
        bne.s   wms_ok                   ; ... not correct cursor key
        jsr     wm_altps                 ; ... yes, find alt pan or scroll
        beq.s   wms_ok                   ; ... not controlled

        move.l  d0,-(sp)
        jsr     wm_clrci                 ; clear current item
        move.l  (sp)+,d0
        btst    #k..shift,d2             ; shift set?
        beq.s   wms_seve                 ; ... no

wms_sxtra
        bset    #wsi..xt,d0              ; extra
wms_seve
        moveq   #pt..scrl,d4             ; scroll event
        btst    #wsi..pan,d0             ; is it?
        beq.s   wms_sd4                  ; ... yes
        moveq   #pt..pan,d4              ; no, must be pan
wms_sd4
        move.l  d4,stk_d4(sp)
        bra.s   wms_exit

wms_ok
        moveq   #0,d0
wms_exit
        tst.l   d2                       ; in arrows?
        bpl.s   wms_ex1
        moveq   #-1,d3                   ; ... yes, mark it
wms_ex1
        movem.l (sp)+,reglist 
        tst.l   d0
wms_rts
        rts
        end
