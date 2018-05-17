; Release slave blocks   V2.00     1986  Tony Tebby   QJUMP

        section mem

        xdef    mem_rlsb

        include 'dev8_keys_sys'
        include 'dev8_keys_sbt'
;+++
; Release slave blocks
;
;       d0   s  slave block table index
;       d1 c  p space to be released
;       a0 c  p address of first slave block
;       a1   s  pointer to slave block tables
;       a6 c  p base of system variables
;
;       no error return, d0 is scratch (should be zero if D1 call correct)
;       all other registers preserved
;---
mem_rlsb
        move.l  sys_sbtb(a6),a1          ; base of slave block table
        move.l  a0,d0
        sub.l   a6,d0                    ; offset of slave block
        lsr.l   #sbt.shft,d0             ; index to table
        add.l   d0,a1                    ; first slave block
        move.l  d1,d0
        lsr.l   #6,d0

mrl_loop
        move.b  #sbt.mpty,sbt_stat(a1)   ; set empty
        addq.l  #sbt.len,a1              ; next slave block table entry
        subq.l  #sbt.size/64,d0
        bgt.s   mrl_loop                 ; ... but still some
        rts
        end
