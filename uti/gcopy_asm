; General copy routine                   1992 Jochen Merz

        include dev8_uti_gcopy_data

        section utility

        xdef    ut_gcopy

        xref    ut_cpyst

;+++
; This routine copies all type of data. It works on a table with entries,
; 6 bytes long each. The table is terminated with a zero word.
; The format of an entry is:
;       byte    source/destination mode
;       byte    type of item to copy
;       word    source
;       word    destination
; The first byte indicates the addressing mode of the source and destination:
;       Bit set       mode
;          5            source is rel a6
;          4            source is rel a5
;          otherwise    source is rel to itself
;          1            destination is rel a6
;          0            destination is rel a5
;          otherwise    destination is rel to itself
; The type of item is -1 for a string or a positive number (e.g. 1 for byte,
; 2 for word, 4 for longword etc.).
;
;               Entry                           Exit
;       a3      pointer to table                smashed
;       a5      supplied if required            preserved
;       a6      supplied if required            preserved
;---
ut_gcopy
        movem.l d0-d2/a0-a1,-(sp)
gcpy_loop
        move.b  (a3)+,d1
        move.b  (a3)+,d0                ; get type of item
        beq.s   gcpy_end
        move.w  (a3),a1                 ; source offset
        btst    #cpy..sa5,d1            ; rel a5?
        bne.s   src_rea5
        btst    #cpy..sa6,d1            ; rel a6?
        bne.s   src_rea6
        add.l   a3,a1                   ; make it relative to itself
        bra.s   src_rel
src_rea5
        add.l   a5,a1
        bra.s   src_rel
src_rea6
        add.l   a6,a1
src_rel
        addq.w  #2,a3                   ; now on to destination offset
        move.w  (a3),a0
        btst    #cpy..da5,d1            ; rel a5?
        bne.s   dst_rea5
        btst    #cpy..da6,d1            ; rel a6?
        bne.s   dst_rea6
        add.l   a3,a0
        bra.s   dst_rel
dst_rea5
        add.l   a5,a0
        bra.s   dst_rel
dst_rea6
        add.l   a6,a0
dst_rel
        addq.w  #2,a3                   ; point to next item in table
        tst.b   d0                      ; string copy?
        bpl.s   no_string               ; no
        bsr     ut_cpyst                ; yes, so do it
        bra.s   gcpy_loop
no_string
        move.b  (a1)+,(a0)+             ; copy byte by byte
        subq.b  #1,d0
        bne.s   no_string
        bra.s   gcpy_loop
gcpy_end
        movem.l (sp)+,d0-d2/a0-a1
        rts

        end
