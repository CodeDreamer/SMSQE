; Jump to Thing Utility through HOTKEY System II
; Copyright 1989 Tony Tebby / Jochen Merz

        section gen_util

        xdef    gu_thjmp
        xref    gu_thvec

        include dev8_keys_thg
;+++
; Jump to Thing Utility through HOTKEY System II.
; Note this only works if a HOTKEY System version 2.03 or later is present.
;
;               Entry                           Exit
;       d1      owner                           Job ID
;       d2      priority/timeout                preserved
;       a0      thing name                      preserved
;       a1      parameter string                preserved
;
;       Condition codes set
;---
gu_thjmp
        move.l  a4,-(sp)
        move.l  d0,-(sp)
        moveq   #thh_entr,d0            ; thing vector required
        bsr.s   gu_thvec                ; get THING vector
        bne.s   gut_ex4                 ; there's nothing to jump to!
        move.l  (sp)+,d0
        jsr     (a4)                    ; do it
gut_exit
        move.l  (sp)+,a4
        tst.l   d0
        rts
gut_ex4
        addq.l  #4,sp                   ; skip operation
        bra.s   gut_exit
        end
