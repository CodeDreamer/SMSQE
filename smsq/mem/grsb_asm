* Grab slave blocks   V2.01     1986  Tony Tebby   QJUMP
*
        section mem
*
        xdef    mem_grsb
*
        include dev8_keys_sys
        include dev8_keys_sbt
        include dev8_keys_iod
*
*       d0   s
*       d1 c  p space required
*       a0 c  p address of first slave block
*       a6 c  p base of system variables
*
*       no error return, d0 is scratch
*       all other registers preserved
*
mem_grsb
mgr.reg reg     d1/a1
        movem.l mgr.reg,-(sp)                save length
        move.l  sys_sbtb(a6),a1         base of slave block table
        move.l  a0,d0
        sub.l   a6,d0                   offset of slave block
        lsr.l   #sbt.shft,d0            index to table
        add.l   d0,a1                   first slave block
*
mgr_loop
        moveq   #sbt.actn,d0            check action bits
        and.b   sbt_stat(a1),d0         action flagged?
        beq.s   mgr_grab                ... no, grab it
*
* force slaving
*
        movem.l d1-d3/a0-a4,-(sp)       save smashable registers
        moveq   #0,d0                   find driver
        move.b  sbt_stat(a1),d0
        lsr.b   #4,d0                   blot out status bits
        lsl.b   #2,d0                   and index in long words
        lea     sys_fsdd(a6),a2         ... the device driver table
        move.l  (a2,d0.l),a2            to get the physical definition block
        move.l  iod_drlk(a2),a3
        lea     -iod_iolk(a3),a3        base of linkage block
        move.l  iod_fslv(a3),a4
        jsr     (a4)                    forced slaving routine
        movem.l (sp)+,d1-d3/a0-a4       recover smashable registers
*
        bra.s   mgr_loop                check it again
mgr_grab
        clr.b   sbt_stat(a1)            clear status
        addq.l  #sbt.len,a1             next slave block table entry
        sub.l   #sbt.size,d1
        bgt.s   mgr_loop                ... but still some
*
        movem.l (sp)+,mgr.reg           restore additional length
        rts
        end
