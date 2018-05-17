; Use Thing Utility through HOTKEY System II
; Copyright 1989 Jochen Merz

        section utility

        xdef    ut_thjmp
        xref    ut_thvec

        include dev8_keys_qdos_sms
        include dev8_keys_thg
        include dev8_keys_err
        include dev8_keys_sys
;+++
; Use Thing Utility through HOTKEY System II.
; Note this only works if a HOTKEY System version 2.03 or later is present.
;
;               Entry                           Exit
;       d0      function code                   ???
;       d1-d3   parameters                      parameters ???
;       a0-a2   parameters                      parameters ???
;
;       Error returns:  err.nimp                THING does not exist
;                       all Thing error returns
;       Condition codes set
;---
ut_thjmp
        move.l  a4,-(sp)
        move.l  d0,-(sp)                ; keep Thing function code
        bsr     ut_thvec                ; get THING vector
        bne.s   jmp_nimp                ; there's nothing to jump to!
        move.l  (sp)+,d0                ; get function code
        jsr     (a4)                    ; do it
        bra.s   jmp_ok
jmp_nimp
        addq.l  #4,sp                   ; adjust stack
jmp_ok
        move.l  (sp)+,a4
        tst.l   d0
        rts

        end
