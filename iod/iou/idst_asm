; IO Device Linkage Setup   V2.01    1985  Tony Tebby   QJUMP

        section iou

        xdef    iou_idst
        xdef    iou_lksv

        xref    gu_achpp

        include 'dev8_keys_iod'

;+++
; IO device driver initialisation. Allocates the linkage block
; and then sets the standard pointers. If A3 points to a zero long word,
; then it is assumed that the linkage block is already allocated and pointed
; to by A0.
;
;      (a0 c  p pointer to linkage block only if (a3) is zero)
;       a3 cr   pointer to linkage block definition in form of
;                   long            length of linkage block
;                   word relative pointers to   external interrupt *
;                                               polling *
;                                               scheduler *
;                                               IO +
;                                               open +
;                                               close +
;
; * If the relative pointer is zero, then no routine will be linked in.
; + If the relative pointer is zero, then the other IO pointers must be zero.
;
;       a3  r   pointer to linkage block
;
;       status return standard
;---
iou_idst
iis.reg reg     d1/a0/a1/a4
        movem.l iis.reg,-(sp)
        move.l  (a3)+,d0                 ; length of linkage block
        beq.s   iis_set                  ; ... preallocated
        jsr     gu_achpp                 ; allocate 
        bne.s   iis_exit
iis_set
        move.l  a0,a1

        bsr.s   iis_lksv                 ; link in the servers
        bsr.s   iis_lksv
        bsr.s   iis_lksv
        bsr.s   iis_lksv                 ; and the IO
        bsr.s   iis_lkxt                 ; ... with two extra entry points
        bsr.s   iis_lkxt
        move.l  a0,a3
        moveq   #0,d0
iis_exit
        movem.l (sp)+,iis.reg
        rts
;+++
; Link in server
;
;       a1 c  u pointer to linkage, updated by 8
;       a3 c  u points to word relative pointer to server (or points to zero)
;       a4   s  scratch
;
;       This routine is unclean.
;---
iou_lksv
iis_lksv
        addq.l  #4,a1
iis_lkxt
        addq.l  #4,a1                    ; skip this entry!!!
        move.l  a3,a4
        move.w  (a3)+,d0
        add.w   d0,a4                    ; absolute address
        beq.s   iis_rts
        move.l  a4,-4(a1)                ; set server address
iis_rts
        rts
        end
