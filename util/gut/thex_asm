; Execute Thing Utility through HOTKEY System II

        section gen_util

        xdef    gu_thex

        xref    gu_thexn

;+++
; Execute Thing Utility through HOTKEY System II.
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
gu_thex
        move.l  a2,-(sp)
        sub.l   a2,a2                    ; no job name
        jsr     gu_thexn
        move.l  (sp)+,a2
        rts
        end
