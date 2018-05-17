; write out error, wait for a keystroke and die

        section utility

        include dev8_keys_qdos_io
        include dev8_keys_qlv

        xdef    ut_erwin

        xref    gu_iow
        xref    gu_die
        xref    met_erms

;+++
; Write an error to a given channel (which is redefined by this routine)
; wait for a confirmation keystroke and die.
;
;               Entry                           Exit
;       d0.l    error code                      ???
;       a0      channel ID                      ???
;---
ut_erwin
        move.l  d0,a4           ; keep error code
        lea     err_wdef,a1
        moveq   #7,d1
        moveq   #1,d2
        moveq   #iow.defw,d0
        bsr     gu_iow           ; define small window
        moveq   #iow.clra,d0
        bsr     gu_iow           ; clear it
        lea     met_erms,a1
        move.w  ut.wtext,a2
        jsr     (a2)
        move.l  a4,d0
        move.w  ut.werms,a2
        jsr     (a2)            ; write out error message
        moveq   #iow.ecur,d0    ; enable cursor
        bsr     gu_iow
        moveq   #iob.fbyt,d0    ; and fetch a keystroke
        bsr     gu_iow
        jmp     gu_die          ; then die

err_wdef
        dc.w    380,32,66,100

        end
