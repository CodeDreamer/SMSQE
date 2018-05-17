; Read name / edit name (extended version)      1992 Jochen Merz V0.01

        section wman

        xdef    wm_rname        ; read name
        xdef    wm_ename        ; edit name

        include dev8_keys_k
        include dev8_keys_err
        include dev8_keys_qdos_io

;+++
; Read or edit name with extended edit features.
; Fully call compatible with WM.ENAME and WM.RNAME.
;
;               Entry                           Exit
;       d1.b                                    terminating character
;       a0      channel ID                      preserved
;       a1      pointer to string               preserved
;
;       any I/O subsystem errors, >0 if terminator not <NL>
;
; The keystrokes supported are:
;   Cursor left & right, Delete to left & right
;   Delete to start and end of line (standard and short)
;   Clear whole line (UNDO)
;   Insert any character code by typing INSERT and a two-digit hex code.
;---

wmr_reg reg     d2-d7/a1-a4

wm_rname
        movem.l wmr_reg,-(sp)
        moveq   #0,d7           ; this is read name
        bra.s   wmr_begin
wm_ename
        movem.l wmr_reg,-(sp)
        moveq   #-1,d7          ; and this is edit name
wmr_begin
        move.l  a1,a4           ; better register for buffer
        move.l  4(a1),-(sp)
        move.l  (a1),-(sp)      ; save buffer contents
        moveq   #iow.chrq,d0
        bsr.l   wmr_doio        ; and find window size
        bne.s   wmr_rstbuf      ; if failed, restore buffer
        move.l  a4,a1
        move.w  (a1),d2         ; width

        moveq   #iow.pixq,d0    ; ... now pixel enquiry
        bsr.l   wmr_doio
        bne.s   wmr_rstbuf      ; if failed, restore buffer
        move.l  a4,a1
        moveq   #0,d1
        move.w  (a1),d1
        divu    d2,d1           ; character spacing
        move.l  d1,d4           ; keep character spacing
        moveq   #0,d6
        move.w  (a1),d6
        sub.w   4(a1),d6        ; actual room in buffer
        divu    d1,d6           ; ... in characters
        move.l  4(a1),d5        ; offsets
wmr_rstbuf
        move.l  (sp)+,(a1)+     ; restore name
        move.l  (sp)+,(a1)
        tst.l   d0
        bne     wmr_return      ; return in case, earlier IO failed

        moveq   #err.bffl,d0    ; assume buffer too long
        cmp.w   (a4),d6
        bls     wmr_return

        moveq   #iow.clrl,d0    ; clear cursor line
        bsr     wmr_ios1        ; do IO and test
        move.l  a4,a1
        move.w  (a1)+,d2
        moveq   #iob.smul,d0
        bsr     wmr_ios1        ; write out string

        and.w   (a4),d7         ; if edit, that's the cursor position
        bne.s   wmr_loop        ; ... and enter loop

        bsr     wmr_poscur      ; position cursor at first character
        bsr     wmr_fbyte       ; fetch first byte
        cmp.b   #k.space,d2     ; is it space?
        bne.s   wmr_noterm      ; no, so that's not a terminator
        moveq   #k.enter,d2     ; remeber, fbyte returns key in d2!

wmr_noterm
        cmp.b   #k.space,d2     ; printable?
        blo.s   wmr_pbyte       ; no, so process byte in loop
        cmp.b   #k.maxch,d2
        bhi.s   wmr_pbyte
        moveq   #iow.clrl,d0
        bsr     wmr_ios1        ; clear cursor line
        clr.w   (a4)            ; and throw away buffer
        bra.s   wmr_pbyte       ; then process byte

wmr_loop
        bsr     wmr_fbyte       ; fetch next character
wmr_pbyte
        cmp.b   #k.bspce,d2     ; backspace?
        beq     wmr_bspce       ; yes, do a backspace
        cmp.b   #k.enter,d2     ; enter?
        beq.s   wmr_done        ; yes, so return
        cmp.b   #k.esc,d2       ; escape?
        beq.s   wmr_done        ; yes, return
        cmp.b   #k.space,d2
        blo.s   wmr_loop        ; forget all other characters below space
        cmp.b   #k.maxch,d2     ; printable?
        bhi     wmr_cursor      ; yes, must be a cursor operation
        moveq   #-2,d0
        add.w   d6,d0
        cmp.w   (a4),d0         ; buffer full?
        blo.s   wmr_loop
        move.w  (a4),d0
        lea     2(a4,d0.w),a1
        sub.w   d7,d0           ; so many characters to move up
        bra.s   wmr_munext
wmr_muloop
        move.b  -(a1),1(a1)     ; move up char by char
wmr_munext
        dbra    d0,wmr_muloop
        move.b  d2,(a1)         ; insert character
        addq.w  #1,(a4)         ; and increment number of chars in string

        move.w  (a4),d2
        sub.w   d7,d2           ; so many characters to print
        addq.w  #1,d7           ; cursor to next position
wmr_prtpos
        moveq   #iob.smul,d0    ; write them out
wmr_iopos
        bsr     wmr_ios1        ; do io operation
        bsr     wmr_poscur      ; position cursor
        bra     wmr_loop        ; and back into loop

wmr_done
        move.w  (a4),d3         ; get length
        moveq   #k.enter,d6
        move.b  d6,2(a4,d3.w)   ; insert terminating character
        moveq   #0,d1
        move.b  d2,d1
        cmp.w   d6,d1           ; set terminator flags
wmr_return
        movem.l (sp)+,wmr_reg
        rts

wmr_cleft
        tst.w   d7              ; already at start of line?
        beq     wmr_loop
        subq.w  #1,d7           ; one character left
        bra.s   wmr_posloop     ; position cursor and enter loop

wmr_cright
        cmp.w   (a4),d7         ; already at end of line?
        beq     wmr_loop
        addq.w  #1,d7           ; one character right
        bra.s   wmr_posloop     ; position cursor and enter loop

wmr_cstart
        clr.w   d7              ; set cursor to start
wmr_posloop
        bsr     wmr_poscur
        bra     wmr_loop

wmr_delst
        tst.w   d7              ; cursor at start of line?
        beq     wmr_loop
        move.l  a4,a1
        move.w  (a1)+,d0
        move.l  a1,a2
        add.w   d7,a1           ; copy from here down
        sub.w   d7,d0           ; so many characters to move
        move.w  d0,(a4)         ; .. means that string will be so long
        bra.s   wmr_mddnext
wmr_mddloop
        move.b  (a1)+,(a2)+     ; copy down
wmr_mddnext
        dbra    d0,wmr_mddloop
        clr.w   d7              ; set cursor to start
        bsr.s   wmr_poscur
        move.l  a4,a1
        move.w  (a1)+,d2
        moveq   #iob.smul,d0
        bsr     wmr_ios1
        moveq   #iow.clrr,d0
        bra     wmr_iopos

wmr_cend
        move.w  (a4),d7         ; cursor to end of line
        bra     wmr_posloop     ; position cursor and enter loop


wmr_delend
        move.w  d7,(a4)         ; cursor position is end of string
        moveq   #iow.clrr,d0
        bsr     wmr_ios1       ; clear right hand side
        bra     wmr_loop

wmr_undo
        clr.w   (a4)            ; clear string
        clr.w   d7              ; cursor at start of line
        moveq   #iow.clrl,d0
        bra     wmr_iopos

wmr_cdel
        cmp.w   (a4),d7         ; cursor alrady behind last character?
        beq     wmr_loop
        addq.w  #1,d7           ; move cursor right ... and into backspace
wmr_bspce
        tst.w   d7              ; already at left margin?
        beq     wmr_loop
        move.w  (a4),d0
        subq.w  #1,d7           ; move cursor to left
        lea     2(a4,d7.w),a1
        sub.w   d7,d0           ; so many characters to move down
        movem.l d0/a1,-(sp)
        bra.s   wmr_mdnext
wmr_mdloop
        move.b  1(a1),(a1)+     ; move characters down
wmr_mdnext
        dbra    d0,wmr_mdloop
        movem.l (sp)+,d2/a1
        move.b  #' ',-1(a1,d2.w) ; add a space to the end
        subq.w  #1,(a4)          ; take off character from string
        bsr.s   wmr_poscur
        bra     wmr_prtpos       ; print string and position cursor again

wmr_poscur
        move.w  d2,d3           ; we need D2 for IOB.SMUL call after return
        move.w  d7,d1
        mulu    d4,d1           ; character position in pixels
        swap    d5
        add.w   d5,d1
        swap    d5
        move.w  d5,d2
        moveq   #iow.spix,d0    ; position cursor
        bsr     wmr_ios2
        move.w  d3,d2
        rts

wmr_cursor
        move.b  d2,d1
        sub.b   #$c0,d1         ; is it cursor left?                    C0
        beq     wmr_cleft
        subq.b  #1,d1           ; is it cursor to start of line?        C1
        beq     wmr_cstart
        subq.b  #1,d1           ; is it backspace?                      C2
        beq     wmr_bspce
        subq.b  #1,d1           ; is it delete to start? (with ALT)     C3
        beq     wmr_delst
        subq.b  #3,d1           ; is it delete to start?                C6
        beq     wmr_delst
        subq.b  #2,d1           ; is it cursor right?                   C8
        beq     wmr_cright
        subq.b  #1,d1           ; is it cursor to end of line?          C9
        beq     wmr_cend
        subq.b  #1,d1           ; is it delete?                         CA
        beq     wmr_cdel
        subq.b  #1,d1           ; is it delete to end? (with ALT)       CB
        beq     wmr_delend
        subq.b  #3,d1           ; is it delete to end of line?          CE
        beq     wmr_delend
        subq.b  #2,d1           ; is it cursor up?                      D0
        beq     wmr_done
        subq.b  #3,d1           ; is it UNDO?                           D3
        beq     wmr_undo
        subq.b  #5,d1           ; is it cursor down?                    D8
        beq     wmr_done
        sub.b   #36,d1          ; is it insert?                         FC
        bne     wmr_loop

wmr_insert
        bsr.s   wmr_fbyte       ; fetch a hex byte
        bsr.s   cnv_htob        ; convert into byte
        bmi     wmr_loop
        move.b  d2,d3
        lsl.b   #4,d3           ; high nibble
        bsr.s   wmr_fbyte       ; fetch next one
        bsr.s   cnv_htob
        bmi     wmr_loop
        or.w    d2,d3           ; low nibble
        move.b  d3,d2
        bra     wmr_pbyte       ; process the byte

cnv_htob
        sub.b   #'0',d2
        bcs.s   cnv_reterr
        cmp.b   #9,d2
        bls.s   cnv_ret
        sub.b   #'A'-'9'-1,d2
        bcs.s   cnv_reterr
        bclr    #5,d2           ; upper-case it
        cmp.b   #$f,d2
        bls.s   cnv_ret
cnv_reterr
        moveq   #-1,d2
cnv_ret
        tst.b   d2
        rts


wmr_fbyte
        moveq   #iow.ecur,d0
        bsr.s   wmr_ios2
        moveq   #iob.fbyt,d0
        bsr.s   wmr_ios2
        move.b  d1,d2
        moveq   #iow.dcur,d0
        bsr.s   wmr_ios2
        rts

wmr_ios2
        bsr.s   wmr_doio        ; do io
        beq.s   wmr_ioret
        addq.l  #8,sp           ; adjust stack
        bra     wmr_return

wmr_ios1
        bsr.s   wmr_doio        ; do io
        beq.s   wmr_ioret       ; succesfull, return
        addq.l  #4,sp           ; adjust stack
        bra     wmr_return      ; and leave routine

wmr_doio
        move.l  d3,-(sp)
        moveq   #forever,d3
        trap    #do.io
        move.l  (sp)+,d3
        tst.l   d0
wmr_ioret
        rts

        end
