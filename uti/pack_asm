; Pack and Unpack files                 1992 Jochen Merz

        include dev8_keys_qdos_io

        section utility

        xdef    ut_pack
        xdef    ut_unpack

        xref    ut_alchp
        xref    ut_rechp
        xref    gu_iow

qtif.id equ     'QTIF'

;+++
; Pack a file into another one.
;
;               Entry                   Exit
;       a2      source channel ID       preserved
;       a3      dest channel ID         preserved
;---
ut_pack
        move.l  #256,d1
        bsr     ut_alchp        ; allocated buffer
        bne     pk_ret
        move.l  a0,a5           ; buffer pointer

        move.l  a5,a1           ; buffer
        move.l  a2,a0           ; source ID
        moveq   #64,d2          ; header length
        moveq   #iof.rhdr,d0
        bsr     gu_iow           ; read header
        bne     pk_relbf
        move.l  (a5),d7         ; number of bytes to convert
        beq     pk_relbf        ; empty source file

        lea     qtif_id,a1
        moveq   #4,d2
        move.l  a3,a0           ; dest ID
        moveq   #iob.smul,d0
        bsr     gu_iow           ; write ID first
        bne     pk_relbf

        moveq   #0,d6           ; pointer through source file
pk_loop
        move.l  d6,d1           ; absolute pointer
        move.l  a2,a0           ; source ID
        moveq   #iof.posa,d0
        bsr     gu_iow           ; position absolute
        bne     pk_relbf

        move.l  d7,d5           ; total number
        sub.l   d6,d5           ; minus current position
        cmp.l   #127,d5         ; maximum package size
        bls.s   pk_load         ; OK, fits
        moveq   #127,d5         ; use maximum size instead
pk_load
        move.l  d5,d2           ; number of bytes to load
        move.l  a5,a1           ; base of buffer
        moveq   #iob.fmul,d0
        bsr     gu_iow           ; fill buffer
        bne     pk_relbf

        move.w  d5,d3           ; countdown
        moveq   #0,d2           ; now find first pos of 3 repeated chars
pk_find3
        cmp.w   #3,d3           ; three chars left?
        ble.s   pk_fndnx        ; no, just next one
        move.b  (a5,d2.w),d1
        cmp.b   1(a5,d2.w),d1   ; same character?
        bne.s   pk_fndnx        ; no, next
        cmp.b   2(a5,d2.w),d1   ; still same character?
        bne.s   pk_fndnx
        tst.l   d2              ; repeated chars at beginning?
        bne.s   pk_write        ; no, so write all up to here
pk_fnddf
        tst.w   d3              ; end of countdown?
        beq.s   pk_wrrep
        cmp.b   1(a5,d2.w),d1   ; still same character?
        bne.s   pk_wrrep        ; no, so write out this number
        addq.w  #1,d2           ; another one found
        subq.w  #1,d3           ; count down
        bra.s   pk_fnddf
pk_wrrep
        addq.w  #1,d2           ; add first char
        add.l   d2,d6           ; these chars are done
        move.l  a3,a0           ; dest ID
        exg     d2,d1           ; number is d1 now
        neg.b   d1              ; negative count
        moveq   #iob.sbyt,d0
        bsr     gu_iow           ; write it
        bne     pk_relbf
        move.b  d2,d1           ; the character
        moveq   #iob.sbyt,d0
        bsr     gu_iow           ; write it
        bne     pk_relbf
        bra.s   pk_nxloop
pk_fndnx
        addq.w  #1,d2           ; one more to save
        subq.w  #1,d3           ; count down
        bne.s   pk_find3
pk_write
        add.l   d2,d6           ; these chars are done
        move.l  a3,a0           ; dest ID
        move.b  d2,d1           ; so many chars
        moveq   #iob.sbyt,d0
        bsr     gu_iow           ; write repetition count
        bne     pk_relbf
        move.l  a5,a1           ; buffer
        moveq   #iob.smul,d0
        bsr     gu_iow           ; now write characters
        bne     pk_relbf
pk_nxloop
        cmp.l   d6,d7           ; file done?
        bne     pk_loop         ; no, so loop
pk_relbf
        move.l  a5,a0           ; release buffer
        bsr     ut_rechp
pk_ret
        rts

ut_unpack
        rts

qtif_id dc.l    qtif.id

        end
