; Directory Device Linkage Setup   V2.01    1985  Tony Tebby   QJUMP

        section iou

        xdef    iou_ddst

        xref    iou_io
        xref    iou_open
        xref    iou_close
        xref    iou_frmt
        xref    iou_load
        xref    iou_save

        xref    gu_achpp

        include 'dev8_keys_iod'

;+++
; Directory device driver initialisation. Allocates the linkage block
; and then sets the standard pointers. If A3 points to a zero long word,
; then it is assumed that the linkage block is already allocated and pointed
; to by A0.
;
;      (a0 c  p pointer to linkage block only if (a3) is zero)
;       a3 cr   pointer to linkage block definition in form of
;                   long            length of linkage block
;                   long            length of physical definition block
;                   word + 4 bytes  name (usage) (max 4 character string)
;                   word + 4 bytes  name (actual) (max 4 character string)
;                   word relative pointers to   external interrupt *
;                                               polling *
;                                               scheduler *
;                                               IO +
;                                               open +
;                                               close +
;                                               forced slaving #
;                                               dummy #
;                                               dummy #
;                                               format +
;
;                             standard vector   check #
;                                               flush #
;                                               information
;                                               load
;                                               save
;                                               truncate
;                                               locate buffer
;                                               allocate buffer
;                                               update buffer #
;                                               allocate first sector
;                                               check for open #
;                                               format drive
;                                               read sector #
;                                               write sector #
;                                         any   device specific #
;                                               -1
;
; * If the relative pointer is zero, then no routine will be linked in.
; + If the relative pointer is zero, a suitable default will be used.
; # If the relative pointer is zero, a dummy will be used.
;
;       a3  r   pointer to linkage block
;
;       status return standard
;---
iou_ddst
ids.reg reg     d1/a0/a1/a2/a4
        movem.l ids.reg,-(sp)
        move.l  (a3)+,d0                 ; length of linkage block
        beq.s   ids_set                  ; ... pre-allocated
        jsr     gu_achpp                 ; allocate
        bne.s   ids_exit
ids_set
        move.l  a0,a1

        lea     iod_plen(a0),a2
        move.l  (a3)+,(a2)+              ; length of physical definition
        move.l  (a3)+,(a2)+              ; length of name
        move.l  (a3)+,(a2)+              ; up to four characters (zero filled)
        move.l  (a3)+,(a2)+

        bsr.s   ids_lksv                 ; link in the servers
        bsr.s   ids_lksv
        bsr.s   ids_lksv

        addq.l  #4,a1                    ; skip the IO linkage
        moveq   #(iod_frmt-iod_ioad)/4,d1 ;number of entries to fill
        lea     ids_dummy,a2

ids_setd
        move.l  a2,a4
        add.w   (a2)+,a4                 ; default address
        move.w  (a3),d0                  ; specified address
        beq.s   ids_seta
        move.l  a3,a4
        add.w   d0,a4
ids_seta
        move.l  a4,(a1)+                 ; set absolute address
        addq.l  #2,a3                    ; next address
        dbra    d1,ids_setd

        add.w   #iod_chek-iod_frmt-4,a1  ; start of second lot

ids_setv
        move.l  a2,a4
        add.w   (a2),a4                  ; dummy address
        move.w  (a3),d0                  ; specified address
        beq.s   ids_setj
        move.l  a3,a4
        add.w   d0,a4
        lsr.w   #1,d0                    ; odd address?
        bcs.s   ids_done                 ; ... yes, done

ids_setj
        move.w  #$4ef9,(a1)+             ; JMP xxx.l
        move.l  a4,(a1)+                 ; set absolute address
        addq.l  #2,a3                    ; next address
        bra     ids_setv

ids_done
        move.l  a0,a3                    ; set linkage address
        moveq   #0,d0                    ; all ok
ids_exit
        movem.l (sp)+,ids.reg
        rts

; link in servers

ids_lksv
        addq.l  #8,a1                    ; skip this entry!!!
        move.l  a3,a4
        move.w  (a3)+,d0
        add.w   d0,a4                    ; absolute address
        beq.s   ids_rts
        move.l  a4,-4(a1)                ; set server address
ids_rts
        rts

; vector of dummy routines

ids_dummy
        dc.w    iou_io-*
        dc.w    iou_open-*
        dc.w    iou_close-*
        dc.w    ids_rts0-*
        dc.w    ids_rts0-*
        dc.w    ids_rts0-*
        dc.w    iou_frmt-*

        dc.w    ids_rts0-*

ids_rts0
        moveq   #0,d0                    ; OK
        rts
        end
