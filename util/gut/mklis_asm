; Make a little list...                 v0.02   Apr 1988  J.R.Oakley  QJUMP

        section gen_util

        include 'dev8_keys_err'
        include 'dev8_keys_chunk'
        include 'dev8_keys_chp'

        xref    gu_achpj
        xref    gu_rchp

        xdef    gu_adlis
        xdef    gu_mklis
        xdef    gu_prlis
        xdef    gu_rtlis

;+++
; Add to list make by GU_MKLIS
;
;       Registers:
;               Entry                           Exit
;       D0.w    entry length (if a1=0)          error code
;       D1                                      number of entries
;       D2                                      modified only by generating code
;       A0      entry generating code
;       A1      pointer to list                 pointer to list
;       A2/A3                                   modified only by generating code
;
;       status return standard
;---
gu_adlis
mklreg  reg     d3-d7/a0/a1/a4/a5
frame   equ     $04
stk_ownr equ    $00
stk_a1  equ     $1c
        move.l  a1,d1                    ; any list?
        beq.s   gu_mklis                 ; ... no start from scratch
        movem.l mklreg,-(sp)
        move.l  chp_ownr-chp.len(a1),-(sp) ; owner of this list

        move.w  chk_entl(a1),d0          ; entry length
        move.w  chk_totl(a1),d3          ; total so far
gal_loop
        move.l  d1,a5                    ; next chunk
        move.l  chk_next(a5),d1
        bne.s   gal_loop

        move.w  chk_nent(a5),d4          ; entries in this chunk
        mulu    d0,d4
        lea     chk.hdrl(a5,d4.l),a1     ; position of next bit

        move.w  chk_maxe(a5),d5
        sub.w   chk_nent(a5),d5          ; entries so far
        bra.s   gml_cont                 ; and continue

;+++
; General list making routine. Given an entry size and a routine to
; create an entry, a linked list of "chunks" with a number of entries
; is created. The chunk format is:
;
; long  next chunk
; word  max entries in this chunk
; word  actual entries in this chunk
; word  entry length (first chunk, but actually in all chunks)
; word  total number of entries (first chunk)
;
;       Registers:
;               Entry                           Exit
;       D0.w    entry length                    error code
;       D1                                      number of entries
;       D2                                      modified only by generating code
;       A0      entry generating code
;       A1                                      pointer to list
;       A2/A3                                   modified only by generating code
;
; The entry generating code should adhere to the following rules:
;
;       Registers:
;               Entry                           Exit
;       D0                                      error or ERR.EOF if no more
;       D1/D2   unused by this routine          used as required
;       D3      entry number (0..n)             preserved
;       A1      space for entry                 preserved
;       A2/A3   unused by this routine          used as required
;---
gu_mklis
        sub.l   a1,a1                   ; no list to start with
        movem.l mklreg,-(sp)
        move.l  #-1,-(sp)               ; list owned by me!

        lea     stk_a1-chk_next(sp),a5  ; where to put first chunk pointer
        moveq   #0,d3                   ; no entries so far
        moveq   #0,d5
gml_cont
        moveq   #$10,d7                 ; sixteen entries per chunk
        move.w  d0,d4
        mulu    d7,d0                   ; chunk size is this
        addq.l  #chk.hdrl/2,d0          ; plus space for header
        addq.l  #chk.hdrl/2,d0
        move.l  d0,d6                   ; keep that
        move.l  a0,a4                   ; keep code pointer
        tst.w   d5
        beq.s   gml_nxtc
gml_loop
        jsr     (a4)                    ; get next entry
        bne.s   gml_chke                ; not OK, finished or error
        addq.w  #1,d3                   ; one more entry
        addq.w  #1,chk_nent(a5)         ; one more entry in this chunk
        add.w   d4,a1                   ; next entry
        subq.w  #1,d5                   ; is there one?
        bne.s   gml_loop                ; yes, fill it in
gml_nxtc
        move.l  d6,d0                   ; no, get this much space
        move.l  stk_ownr(sp),a0         ; for the list owner
        jsr     gu_achpj(pc)
        bne.s   gml_chke                ; ...oops
        move.l  a0,chk_next(a5)         ; link new to the last one (if any)
        exg     a0,a5                   ; new becomes current
        move.w  d7,chk_maxe(a5)         ; max entries this chunk
        move.w  d7,d5
        move.w  d4,chk_entl(a5)         ; length of entry
        lea     chk.hdrl(a5),a1         ; first entry
        bra.s   gml_loop                ; fill it in
gml_chke
        move.l  stk_a1(sp),a1           ; start of list
        cmp.l   #err.eof,d0             ; just EOF?
        bne.s   gml_exrm                ; no, remove chunks
        tst.w   chk_nent(a5)            ; is this chunk used?
        bne.s   gml_exok                ; yes
        clr.l   chk_next(a0)            ; no, clear last link pointer
        move.l  a5,a0                   ; and return last chunk
        jsr     gu_rchp(pc)
gml_exok
        move.w  d3,d1                   ; return total number of entries
        move.w  d1,chk_totl(a1)
        moveq   #0,d0
gml_exit
        addq.l  #frame,sp
        movem.l (sp)+,mklreg
        tst.l   d0
        rts
gml_exrm
        bsr.s   gu_rtlis                ; remove any current list
        bra.s   gml_exit                ; and exit
;+++
; Return a general list, as made with gu_MKLIS, to the heap.
;
;       Registers:
;               Entry                           Exit
;       D0                                      zero
;       A1      pointer to first chunk          zero
;---
gu_rtlis
rtlreg  reg     d0/a0
        movem.l rtlreg,-(sp)
        bra.s   grl_eloop
grl_loop
        move.l  chk_next(a1),a1         ; yes, get next
        move.l  d0,a0
        jsr     gu_rchp(pc)             ; return this one
grl_eloop
        move.l  a1,d0                   ; see if there is one
        bne.s   grl_loop
grl_exit
        movem.l (sp)+,rtlreg
        rts
;+++
; Process a general list, as made with gu_MKLIS.
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       A0      processing code                 preserved
;       A1      list to process (first chunk)   preserved
;
; The processing code should adhere to the following rules:
;
;       Registers:
;               Entry                           Exit
;       D0                                      error code
;       D1-D3   unused by this routine          used as required
;       A1      entry to process                preserved
;       A2-A4   unused by this routine          used as required
;---
gu_prlis
prlreg  reg     d4-d6/a1/a5
        movem.l prlreg,-(sp)
        move.l  a1,d5                   ; and start chunk
        beq.s   gpl_exok
        move.w  chk_entl(a1),d4         ; keep entry length
        bra.s   gpl_nxtc

gpl_loop
        jsr     (a0)                    ; process this entry
        bne.s   gpl_exit                ; ...oops
        add.w   d4,a1                   ; next entry
        subq.w  #1,d6                   ; more entries?
        bne.s   gpl_loop                ; yes
*
        move.l  chk_next(a5),d5         ; next chunk
gpl_nxtc
        beq.s   gpl_exok                ; no more, exit
        move.l  d5,a5                   ; point to chunk
        lea     chk.hdrl(a5),a1         ; point to first entry
        move.w  chk_nent(a5),d6         ; this many entries
        bgt.s   gpl_loop
*
gpl_exok
        moveq   #0,d0
gpl_exit
        movem.l (sp)+,prlreg
        rts
*
        end
