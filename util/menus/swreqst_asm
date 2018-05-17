; Set window to loose menu item and request a string    V2.01   1992 J.Merz

        include dev8_keys_qdos_io
        include dev8_keys_wman

        section utility

        xdef    mu_sweds        ; set window and edit string
        xdef    mu_swrds        ; set window and read string
;+++
; Set window to loose menu item and request name with Read.
;
;               Entry                           Exit
;       d1      loose menu item number          terminating character
;       a0      channel ID                      preserved
;       a2      window manager vector           preserved
;       a3      string buffer                   preserved
;
;       Error returns: input I/O errors
;       Condition codes set on return
;---
rq_reg  reg     d2-d3/d5/a1
mu_swrds
        movem.l rq_reg,-(sp)
        move.w  #wm.rname,d5    ; that's the required action
        bra.s   mu_rqall

;+++
; Set window to loose menu item and request string with Edit.
;
;               Entry                           Exit
;       d1      loose menu item number          preserved
;       a0      channel ID                      preserved
;       a2      window manager vector           preserved
;       a3      string buffer                   preserved
;
;       Error returns: input I/O errors
;       Condition codes set on return
;---
mu_sweds
        movem.l rq_reg,-(sp)
        move.w  #wm.ename,d5    ; that's the required action
mu_rqall
        moveq   #0,d2
        jsr     wm.swlit(a2)    ; set window to loose menu item
        moveq   #0,d1
        moveq   #forever,d3
        moveq   #iow.sova,d0
        trap    #do.io          ; set over mode to normal
        move.l  a3,a1           ; here's the string
        jsr     (a2,d5.w)       ; call routine
        movem.l (sp)+,rq_reg
        tst.l   d0              ; set condition codes
        rts

        end
