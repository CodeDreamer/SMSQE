; ATARI seek to track      1988   Tony Tebby  QJUMP

        section fd

        xdef    fd_seek
        xdef    fd_rest

        xref    fd_cmd

        include dev8_keys_atari

*+++
; Seek to track - direct physical version *********** step rate constant
;
;       d0   s  scratch
;       d3 c  p track number
;
;       status return arbitrary
*---
fd_seek
        move.w  #dma.fdat,dma_mode       ; set data reg
        move.w  d3,dma_data              ; and track required
        beq.s   fd_rest                  ; zero, restore
        move.w  #dma.ftrk,dma_mode       ; already there?
        move.w  dma_data,d0
        cmp.b   d0,d3
        beq.s   fds_rts                  ; ... yes

        moveq   #fdc.seek,d0             ; seek
        bra.s   fds_do
*+++
; Restore to track - direct physical version ************ step rate preset
;
;       d0   s  scratch
;
;       status return arbitrary
*---
fd_rest
        moveq   #fdc.rest,d0             ; restore

fds_do
        move.l  d1,-(sp)
        jsr     fd_cmd

        move.l  #310000,d1               ; wait up to 1.6 s for not busy
fds_wait
        moveq   #1<<fds..bsy,d0          ;              04
        and.w   dma_data,d0              ; still busy?  12
        beq.s   fds_exit                 ;              08
        subq.l  #1,d1                    ;              08
        bgt.s   fds_wait                 ;              10
                                         ;              42=5.25us
fds_exit
        move.l  (sp)+,d1
fds_rts
        rts
        end
