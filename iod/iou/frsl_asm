; IO Utilities Find Free Slave  V1.00    1988   Tony Tebby QJUMP

        section iou

        xdef    iou_frsl

        include 'dev8_keys_sys'
        include 'dev8_keys_sbt'
        include 'dev8_keys_chn'
        include 'dev8_keys_iod'
;+++
; Find free slave block. In this simple version it just takes the
; first slave block it comes to. If this is awaiting write, it flushes it.
; It fills in the current slave block table pointer in the channel block.
; It marks the block as valid to prevent odd actions occurring.
;
;       d0    p
;       a0 c  p channel block
;       a1  r   pointer to slave block table
;       a2  r   pointer slave block
;
;       error status 0
;---
iou_frsl
ifr.reg  reg    d0/d1/d2
        movem.l ifr.reg,-(sp)
        moveq   #sbt.true,d2             ; true status
        ror.l   #4,d2
        move.b  chn_drid(a0),d2
        ror.l   #4,d2                    ; + drive id in msbyte

        moveq   #sbt.len,d1
        add.l   sys_sbrp(a6),d1          ; next slave block
        sub.l   sys_sbtb(a6),d1          ; offset in table
        lsl.l   #sbt.shft,d1
        add.l   a6,d1                    ; pointer to slave block itself
        cmp.l   sys_fsbb(a6),d1          ; below base?
        blt.s   ifr_base
        cmp.l   sys_sbab(a6),d1          ; off top?
        blt.s   ifr_set
ifr_base
        move.l  sys_fsbb(a6),d1          ; start at beginning
ifr_set
        move.l  d1,a2                    ; block itself
        sub.l   a6,d1
        lsr.l   #sbt.shft,d1
        move.l  sys_sbtb(a6),a1
        add.l   d1,a1                    ; slave block pointer
        move.l  a1,chn_csb(a0)
        move.l  a1,sys_sbrp(a6)

ifr_wait
        moveq   #sbt.actn,d0             ; action bits
        and.b   sbt_stat(a1),d0
        beq.s   ifr_ours

        movem.l d1-d3/a0-a4,-(sp)
        moveq   #0,d0
        move.b  sbt_stat(a1),d0          
        lsr.b   #4,d0                    ; drive number
        lsl.b   #2,d0                    ; ... offset in table
        lea     sys_fsdd(a6),a2
        move.l  (a2,d0.w),a2             ; physical definition
        move.l  iod_drlk(a2),a3
        sub.w   #iod_iolk,a3             ; linkage block
        move.l  iod_fslv(a3),a4
        jsr     (a4)                     ; force slave this
        movem.l (sp)+,d1-d3/a0-a4
        bra.s   ifr_wait

ifr_ours
        move.l  d2,(a1)                  ; clear block
        clr.l   4(a1)                    ; and zero status

        movem.l (sp)+,ifr.reg
        rts
        end
