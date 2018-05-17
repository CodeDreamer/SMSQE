; Link in vectored server     1988  Tony Tebby   QJUMP
;
; I am not sure why I do it this way!!
; A bit of code in the linkage
;
;       movem.l ....a3....-(sp)
;       lea     .....(pc),a3
;       jmp     .....l
;
; Occupies only 14 bytes, two bytes more than the linkage used here!
;
        section iou

        xdef    iou_lkvm
        xdef    iou_lkva
;+++
; This routine links in a vectored interrupt server. The server entry code
; preserves all registers. The linkage occupies $0c bytes.
;
;       d1 c  p offset of linkage from base of block (word)
;       a1 cr   pointer to service routine / pointer to server linkage
;       a3 c  p linkage block
;       all other registers (including d0) preserved
;       status returned OK
;---
iou_lkva
        pea     iou_prva                 ; save all regs for server
        move.l  a1,-(sp)                 ; server address
        bra.s   iou_lkv
;+++
; This routine links in a vectored interrupt server. The server entry code
; preserves the minimum of registers (a2/a3). The linkage occupies $0c bytes.
;
;       d1 c  p offset of linkage from base of block (word)
;       a1 cr   pointer to service routine / pointer to server linkage
;       a3 c  p linkage block
;       all other registers (including d0) preserved
;       status returned OK
;---
iou_lkvm
        pea     iou_prvm                 ; save minimum regs for server
        move.l  a1,-(sp)                 ; server address

iou_lkv
        lea     $c(a3,d1.l),a1           ; end address of linkage
        move.l  (sp)+,-(a1)              ; server address
        move.w  #6,-(a1)
        add.w   d1,(a1)                  ; offset from base
        move.l  (sp)+,-(a1)              ; linkage processing code
        move.w  #$4eb9,-(a1)             ; JSR  .L
        cmp.w   d0,d0                    ; cc OK
        rts

; vectored interrupt server processing

iou_prva                 ; all register usage
        movem.l d0-d7/a0-a5,-(sp)        ; all except a6
        move.l  $38(sp),a2               ; pointer to offset
        move.l  a6,$38(sp)               ; now a6 as well
        move.l  a2,a3
        sub.w   (a2)+,a3                 ; linkage base
        move.l  (a2),a2                  ; start address
        jmp     (a2)                     ; d0-d7/a0-a6 on stack

iou_prvm                 ; minimum register usage
        move.l  a2,-(sp)                 ; working reg
        move.l  4(sp),a2                 ; pointer to offset
        move.l  a3,4(sp)                 ; linkage register saved
        move.l  a2,a3
        sub.w   (a2)+,a3                 ; linkage base
        move.l  (a2),a2                  ; start address
        jmp     (a2)                     ; a2/a3 on stack
        end
