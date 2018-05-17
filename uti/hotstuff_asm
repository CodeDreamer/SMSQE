; Stuff HOTKEY buffer                    1991 Jochen Merz   V0.01

        section utility

        include dev8_keys_hk_vector

        xdef    ut_stuff
        xdef    ut_stufc

        xref    ut_ushok
        xref    ut_frhok

;+++
; Stuff a QDOS string into the HOTKEY buffer.
;
;               Entry                   Exit
;       D0                              error
;       A1      ptr to string           saved
;---
ut_stuff
        movem.l d2/a1,-(sp)
        move.w  (a1)+,d2
        bsr.s   ut_stufc

        movem.l (sp)+,d2/a1
        rts

;+++
; Stuff some characters into the HOTKEY buffer.
;
;               Entry                   Exit
;       D0                              error
;       D2      number of characters    smashed
;       A1      ptr to characters
;---
ut_stufc
        movem.l d4/a0-a4,-(sp)
        move.l  a1,a4               ; save chars
        move.w  d2,d4               ; save number of chars

        bsr     ut_ushok
        move.l  a1,a3               ; the Hotkey linkage must be in A3

        move.l  a4,a1               ; restore chars
        bne.s   stf_err

        move.w  d4,d2               ; restore number of chars
        move.l  hk.stbuf(a3),a2
        jsr     (a2)

        bsr     ut_frhok
stf_err
        movem.l (sp)+,d4/a0-a4
        rts

        end
