; Load file into heap    V1.00    1988  Tony Tebby

        section gen_util

        xdef    gu_lchp
        xdef    gu_lchpx
        xdef    gu_lchph
        xdef    gu_lchpf

        xref    gu_iowp
        xref    gu_achp0
        xref    gu_rchp
        xref    gu_fclos

        include 'dev8_keys_hdr'
        include 'dev8_keys_qdos_io'
;+++
; This routine reads the file header, allocates enough heap for the file
; plus its header (full form) plus an extension of a given length.
; plus an extension of the given length, loads the file into the heap
; after the extension, and closes the channel.  If the extension was of
; an odd length then it is evened up before use.
;
;       d1  r   extension required/length of file
;       a0 c    channel ID
;       a0  r   base of heap
;       error returns standard
;       uses    $50 bytes of stack + gu_achp0/gu_rchp/gu_fclos/gu_iowp
;---
gu_lchpf
reglchp reg     d2/d3/a1
frame   equ     hdr.len
        movem.l reglchp,-(sp)
        sub.w   #frame,sp
        moveq   #hdr.len,d2
        bra.s   ugl_ntry
;+++
; This routine reads the file header, allocates enough heap for the file
; plus its header (short form) plus an extension of a given length.
; plus an extension of the given length, loads the file into the heap
; after the extension, and closes the channel.  If the extension was of
; an odd length then it is evened up before use.
;
;       d1  r   extension required/length of file
;       a0 c    channel ID
;       a0  r   base of heap
;       error returns standard
;       uses    $50 bytes of stack + gu_achp0/gu_rchp/gu_fclos/gu_iowp
;---
gu_lchph
        movem.l reglchp,-(sp)
        sub.w   #frame,sp
        moveq   #hdr.set,d2             ; header length to read
        bra.s   ugl_ntry
;+++
; This routine reads the file header, allocates enough heap, loads the
; file and closes the channel.
;
;       d1 cr   length of file
;       a0 c    channel ID
;       a0  r   base of heap
;       error returns standard
;       uses    $50 bytes of stack + gu_achp0/gu_rchp/gu_fclos/gu_iowp
;---
gu_lchp
        moveq    #0,d1                   ; no extension
;+++
; This routine reads the file header, allocates enough heap for the file
; plus an extension of the given length, loads the file into the heap
; after the extension, and closes the channel.  If the extension was of
; an odd length then it is evened up before use.
;
;       d1 cr   extension required/length of file
;       a0 c    channel ID
;       a0  r   base of heap
;       error returns standard
;       uses    $50 bytes of stack + gu_achp0/gu_rchp/gu_fclos/gu_iowp
;---
gu_lchpx
        movem.l reglchp,-(sp)
        sub.w   #frame,sp
        moveq   #4,d2                    ; ... no header required, just length
ugl_ntry
        addq.l  #1,d1                    ; ensure extension is...
        bclr    #0,d1                    ; ...evened up, with no header
        move.l  d1,d3                    ; save complete extension
        add.l   d2,d1                    ; room for header as well

        moveq   #iof.rhdr,d0             ; read header
        lea     (sp),a1                  ; into stack frame
        jsr     gu_iowp
        bne.s   ugl_close

        move.l  a0,a1                    ; save channel
        add.l   (sp),d1                  ; total length
        move.l  d1,d0
        jsr     gu_achp0                 ; allocate it
        exg     a0,a1
        bne.s   ugl_close

        move.l  a1,d1                    ; keep base
        add.l   d3,a1                    ; we want header / file here

        cmp.w   #hdr.set,d2              ; copy header?
        blt.s   ugl_flod                 ; no

        move.l  a0,-(sp)
        lea     4(sp),a0                 ; header is here
ugl_cphl
        move.w  (a0)+,(a1)+              ; copy some header
        subq.w  #2,d2
        bgt.s   ugl_cphl

        move.l  (sp)+,a0

ugl_flod
        moveq   #iof.load,d0             ; load file
        move.l  (sp),d2                  ; length
        jsr     gu_iowp
        move.l  d1,a1                    ; restore heap base
        beq.s   ugl_close

        exg     a0,a1
        jsr     gu_rchp                  ; return heap
        exg     a0,a1

ugl_close
        move.l  d2,d1                    ; set returned length
        jsr     gu_fclos                 ; close file, set CCR
        move.l  a1,a0                    ; set returned base address
        add.w   #frame,sp
        movem.l (sp)+,reglchp
        rts
        end
