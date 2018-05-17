; Save file from heap    V1.00    Apr 1988  J.R.Oakley  QJUMP

        section gen_util

        xdef    gu_schp

        xref    gu_trap3
        xref    gu_fclos

        include 'dev8_keys_hdr'
        include 'dev8_keys_qdos_io'
;+++
; Save a file from the common heap.  The bytes of the file and those of the
; 14-byte header are pointed to individually.
;
;       a0 c    channel ID - closed on exit
;       a1 c p  pointer to file header
;       a2 c p  pointer to file data
;       error returns standard
;       uses    $0C bytes of stack + gu_trap3/gu_fclos
;---
gu_schph
regschp reg     d1/d2/a1
        movem.l regschp,-(sp)
        moveq   #-hdr.set,d2             ; difference between header end...
        sub.l   a1,d2
        add.l   a2,d2                    ; ...and data start is this
        bra.s   ugs_shdr
;+++
; Save a file from the common heap.  The bytes of the file should be immediately
; preceded by a 14-byte header in the standard form.  The heap is not
; released.
;
;       a0 c    channel ID - closed on exit
;       a1 c p  pointer to file header & file data
;       error returns standard
;       uses    $0C bytes of stack + gu_trap3/gu_fclos
;---
gu_schp
        movem.l regschp,-(sp)
        moveq   #0,d2                    ; data follows header

ugs_shdr
        moveq   #iof.shdr,d0             ; set header
        jsr     gu_trap3(pc)
        bne.s   ugs_close                ; ...oops

        move.l  hdr_flen-hdr.set(a1),d1  ; length is in the header
        add.l   d2,a1                    ; skip to data
        move.l  d1,d2                    ; length should be here
        moveq   #iof.save,d0
        jsr     gu_trap3(pc)

ugs_close
        jsr     gu_fclos                 ; close file, set CCR
        movem.l (sp)+,regschp
        rts
        end
