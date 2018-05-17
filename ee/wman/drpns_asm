; Draw PAN and SCROLL bits   V1.02   1986  Tony Tebby  QJUMP

        section wman

        xdef    wm_drpns

        xref    wm_drupa
        xref    wm_drdna
        xref    wm_drlta
        xref    wm_drrta

        include dev8_keys_wwork
        include dev8_keys_wstatus

;       d0  r   error return
;       a0 c  p channel ID of window
;       a1   sp pointer to status area
;       a3 c  p pointer to sub-window definition
;       a4 c  p pointer to working definition

;               all other registers preserved

reglist reg d1-d7/a1-a4

wm_drpns
        move.l  wwa_part+wwa.clen(a3),d0 ; get y part control pointer
        or.l    wwa_part(a3),d0          ; get x part control pointer
        bne.s   wdp_do                   ; ... controlled
        rts

wdp_do
        movem.l reglist,-(sp)
        move.w  wwa_iatt+wwa_curw(a3),d7 ; item border
        move.l  wwa_part(a3),d0          ; any x control?
        beq.s   wdp_yonly                ; ... no
        move.l  d0,a2
        move.w  (a2)+,d6                 ; how many sections?
        ble.s   wdp_yonly                ; ... none
        move.w  d7,d0
        lsl.w   #2,d0                    ; allowance for border
        addq.w  #ww.pnarr,d0
        cmp.w   wwa_xoff(a3),d0          ; ... room for pan arrows?
        bgt.s   wdp_yonly

wdp_xnext
        move.w  wss_spos(a2),d5          ; start
        add.w   d7,d7
        add.w   d7,d5
        lea     wm_drlta,a1              ; draw left arrows
        bsr.s   wdp_dox
        addq.w  #ww.pnarr,d5             ; over a bit
        add.w   d7,d5
        add.w   d7,d5                    ; to section
        addq.l  #wss.ssln,a2
        move.w  wss_spos(a2),d4          ; start of next
        subq.b  #1,d6                    ; last section?
        bgt.s   wdp_xscrol
        move.w  wwa_xsiz(a3),d4          ; end
wdp_xscrol
        subq.w  #ww.pnarr,d4             ; left a bit
        sub.w   d7,d4
        sub.w   d7,d4
        sub.w   d5,d4                    ; width

        lsr.w   #1,d7
        movem.l d6/a2,-(sp)
        bsr.s   wdp_doy
        movem.l (sp)+,d6/a2

        add.w   d7,d7
        add.w   d4,d5                    ; new start
        add.w   d7,d5
        lea     wm_drrta,a1
        bsr.s   wdp_dox                  ; do x
        lsr.w   #1,d7
        tst.w   d6                       ; all done?
        bgt.s   wdp_xnext                ; ... no
        bra.s   wdp_exit                 ; ... yes


wdp_yonly
        add.w   d7,d7
        move.w  d7,d5                    ; start
        move.w  wwa_xsiz(a3),d4          ; full width
        sub.w   d7,d4
        sub.w   d7,d4                    ; width
        lsr.w   #1,d7
        bsr.s   wdp_doy

wdp_exit
        movem.l (sp)+,reglist
        tst.l   d0
wdp_rts
        rts

reg.dox  reg    a2/d6
wdp_dox
        movem.l reg.dox,-(sp)
        swap    d4
        swap    d5
        move.l  wwa_part+wwa.clen(a3),d0 ; y control block
        beq.s   wdp_xonly                ; ... none
        move.l  d0,a2
        move.w  (a2)+,d6                 ; number of parts
        ble.s   wdp_xonly                ; ... none

        addq.w  #ww.scarr,d7

wdp_sxloop
        move.w  wss_spos(a2),d5          ; section start
        add.w   d7,d5                    ; y org

        subq.w  #1,d6                    ; last section?
        ble.s   wdp_sxend
        addq.w  #wss.ssln,a2             ; next section
        move.w  wss_spos(a2),d4          ; end of this
        sub.w   d7,d4
        sub.w   d5,d4
        jsr     (a1)                     ; draw arrows
        bra.s   wdp_sxloop

wdp_sxend
        move.w  wwa_ysiz(a3),d4          ; bottom
        sub.w   d7,d4
        sub.w   d5,d4                    ; size
        subq.w  #ww.scarr,d7             ; restore 2xborder
        bra.s   wdp_xdone

wdp_xonly
        move.w  d7,d5                    ; y start
        move.w  wwa_ysiz(a3),d4          ; y top
        sub.w   d7,d4
        sub.w   d7,d4
wdp_xdone
        jsr     (a1)                     ; draw pan arrows
        swap    d4
        swap    d5

        movem.l (sp)+,reg.dox
        rts

wdp_rok
        moveq   #0,d0
        rts

wdp_doy
        move.l  wwa_part+wwa.clen(a3),d0 ; y control block
        beq.s   wdp_rts                  ; none
        move.l  d0,a2
        move.w  (a2)+,d6                 ; set number of parts
        ble.s   wdp_rok                  ; ... none, give up
        move.w  d7,d0
        add.w   d0,d0
        addq.w  #ww.scarr,d0
        cmp.w   wwa_yoff(a3),d0          ; room for arrows?
        bgt.s   wdp_rok

        swap    d4
        swap    d5
        move.w  wss_spos(a2),d5          ; section start
        add.w   d7,d5                    ; y org
        move.w  #ww.scarr,d4             ; y size

wdp_ssloop
        bsr.l   wm_drupa                 ; draw up arrows
        subq.w  #1,d6                    ; ... last?
        beq.s   wdp_ssend                ; ... yes

        addq.w  #wss.ssln,a2             ; next section
        move.w  wss_spos(a2),d5
        subq.w  #ww.scarr,d5             ; position down arrows
        sub.w   d7,d5
        bsr.l   wm_drdna                 ; and draw them
        addq.w  #ww.scarr,d5             ; position up arrows
        add.w   d7,d5
        add.w   d7,d5
        bra.s   wdp_ssloop

wdp_ssend
        move.w  wwa_ysiz(a3),d5          ; bottom
        sub.w   d7,d5
        subq.w  #ww.scarr,d5             ; not quite !!!

        jsr     wm_drdna                 ; draw last arrows
        swap    d4
        swap    d5
        rts
        end
