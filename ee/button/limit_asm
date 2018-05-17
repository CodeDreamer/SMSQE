; Limit size of button frame to fit in window   V0.00   1989 Tony Tebby

        section config

        xdef    bt_xlim
        xdef    bt_ylim
        xdef    bt_sxlim
        xdef    bt_sylim

        xref    bt_xorg
        xref    bt_yorg
        xref    bt_xsiz

        include 'dev8_keys_wstatus'

;+++
; This routine is a CONFIG post-processing routine to limit the X size of
; a Button frame.
;---
bt_xlim
;+++
; This routine is a CONFIG post-processing routine to limit the Y size of
; a Button frame.
;---
bt_ylim
        move.w  #1024,d0                 ; set limit

        sub.w   (a0),d0                  ; max size
        cmp.w   bt_xsiz-bt_xorg(a0),d0   ; enough room?
        bge.s   btl_ok                   ; ... yes
        move.w  d0,bt_xsiz-bt_xorg(a0)   ; ... no, limit it
btl_ok
        moveq   #wsi.avbl,d1
        moveq   #0,d0                    ; item ok
        rts
;+++
; This routine is a CONFIG pre-processing routine to set the X limit of
; a Button frame.
;---
bt_sxlim
;+++
; This routine is a CONFIG pre-processing routine to set the Y limit of
; a Button frame.
;---
bt_sylim
        move.w  #1024,d0                  ; set limit

        sub.w   bt_xorg-bt_xsiz(a0),d0   ; upper limit
        move.w  d0,2(a2)                 ; set it
        bra.s   btl_ok

        end
