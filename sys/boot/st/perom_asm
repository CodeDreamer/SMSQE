; PEROM executable          1993  Tony Tebby

        section base

        xref    pe_prog                  ; programming code
        xref    end_all                  ; data to program (starts silly long)

        xref    gu_fopen
        xref    gu_clra
        xref    gu_iow
        xref    gu_sstrg
        xref    gu_nl
        xref    gu_die
        xref    gu_fatal
        xref    gu_fater

        include 'dev8_keys_qdos_ioa'
        include 'dev8_keys_qdos_io'
        include 'dev8_keys_atari_perom'

        data    1024

        dc.w    $FAFA,1            ; one address to blat
        dc.l    pe_plen-*

base
        bra.s   perom_start
        dc.w    0,0,$4afb,5,'PEROM'

pe_plen dc.l    base-(end_all+8)         ; add total file length to this to get
                                         ; the length of data to be programmed

pe_con   dc.w   3,'con'
pem_prog dc.b   0,12,'Programming',$a
pem_fail dc.b   0,18,'Programming failed'

perom_start
        lea     pe_con,a0
        moveq   #ioa.kshr,d3
        jsr     gu_fopen                 ; open console
        move.l  a0,-(sp)
pes_con equ     0
        jsr     gu_clra
        moveq   #iow.defb,d0
        moveq   #4,d1
        moveq   #1,d2
        jsr     gu_iow

        moveq   #5,d5                    ; 5 retries

perom_retry
        move.l  pes_con(sp),a0
        lea     pem_prog,a1
        jsr     gu_sstrg

        move.l  a4,a0

        move.w  sr,d7                    ; impossible to interrupt this
        trap    #0

        lea     end_all+8,a1             ; program this
        move.l  pe_plen,d2               ; ... length
        lea     pe_rbase,a2              ; ... to here

        jsr     pe_prog                  ; program

        move.w  d7,sr
        tst.l   d0                       ; OK?
        beq.l   gu_die                   ; ... yes

        move.l  pes_con(sp),a0           ; send fail message
        lea     pem_fail,a1
        jsr     gu_sstrg
        jsr     gu_nl
        subq.l  #1,d5                    ; too many failures?
        bgt     perom_retry              ; ... no, retry

        jmp     gu_fatal

        end
