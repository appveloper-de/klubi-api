package racoony.software.klubi.event_sourcing.bus

import io.kotlintest.shouldBe
import io.kotlintest.specs.DescribeSpec
import racoony.software.klubi.event_sourcing.Event
import racoony.software.klubi.event_sourcing.TestEvent

class EventBusSpec : DescribeSpec({
    describe("event bus") {
        it ("publishes events to subscribed event handlers") {
            val handler = BusTestEventHandler()
            RxEventBus().apply {
                subscribe(BusTestEvent::class.java, handler)
                publish(BusTestEvent("test"))
            }

            handler.message shouldBe "foo"
        }

        it ("does not publish events to other handlers") {
            val handler = BusTestEventHandler()
            RxEventBus().apply {
                subscribe(BusTestEvent::class.java, handler)
                publish(TestEvent())
            }

            handler.message shouldBe ""
        }
    }
})

class BusTestEvent(val value: String) : Event

class BusTestEventHandler() : EventHandler<BusTestEvent> {
    var message = ""
    override fun handle(event: BusTestEvent) {
        this.message += "foo"
    }
}
